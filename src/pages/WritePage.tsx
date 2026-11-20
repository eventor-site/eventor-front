import { useQuery, useQueryClient } from '@tanstack/react-query';
import { lazy, Suspense, useEffect, useRef, useState, type FormEvent } from 'react';
import { Link, Navigate, useNavigate, useParams, useSearchParams } from 'react-router';
import { insightsApi, postsApi, writeApi } from '@/api/endpoints';
import type { PostDetail, PostForm, PostImage } from '@/api/types';
import { useAuth } from '@/auth/AuthContext';
import { Icon } from '@/components/Icon';
import type { RichEditorHandle } from '@/components/RichEditor';
import { useToast } from '@/components/Toast';
import { getViewerId } from '@/lib/viewer';
import { isEventCategory, useCategories } from '@/lib/useCategories';
import { useDocumentTitle } from '@/lib/useDocumentTitle';
import { refreshAfterPostChange } from '@/lib/postCache';
import { BOARDS, LEGACY_CATEGORY_TO_DOMAIN } from '@/lib/domains';

const RichEditor = lazy(() => import('@/components/RichEditor'));

/** 회원이 쓸 수 있는 카테고리(백엔드 PostServiceImpl.MEMBER_WRITABLE_CATEGORIES 와 같게) */
const MEMBER_CATEGORIES = ['자유', '핫딜'];
const END_TYPES = ['상시', '별도 공지 시', '별도 소진 시'];
const MAX_TOTAL = 20 * 1024 * 1024;

const toLocalInput = (iso: string | null | undefined) => (iso ? iso.slice(0, 16) : '');
const fromLocalInput = (value: string) => (value ? `${value}:00` : null);
const formatSize = (bytes: number) =>
  bytes >= 1024 * 1024 ? `${(bytes / 1024 / 1024).toFixed(1)}MB` : `${Math.ceil(bytes / 1024)}KB`;

export function WritePage() {
  const { postId: editId } = useParams();
  const [params] = useSearchParams();
  const navigate = useNavigate();
  const toast = useToast();
  const queryClient = useQueryClient();
  const { isLoggedIn, isAdmin, loading } = useAuth();
  const categories = useCategories();
  const editing = !!editId;
  useDocumentTitle(editing ? '글 수정' : '글쓰기');

  const [postId, setPostId] = useState<number | null>(editId ? Number(editId) : null);
  const [loaded, setLoaded] = useState(!editing);
  const [initialHtml, setInitialHtml] = useState('');
  const [categoryName, setCategoryName] = useState(params.get('categoryName') ?? '');
  const [title, setTitle] = useState('');
  const [link, setLink] = useState('');
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');
  const [endType, setEndType] = useState('');
  const [shoppingMall, setShoppingMall] = useState('');
  const [productName, setProductName] = useState('');
  const [price, setPrice] = useState('');
  const [images, setImages] = useState<PostImage[]>([]);
  const [busy, setBusy] = useState(false);
  const [filling, setFilling] = useState(false);

  /** AI 링크로 채우기: 빈 칸만 채우고, 이미 쓴 내용은 덮어쓰지 않는다 */
  const fillFromLink = async () => {
    setFilling(true);
    try {
      const p = await insightsApi.linkPreview(link.trim());
      if (p.title && !title.trim()) setTitle(p.title);
      if (isEvent) {
        if (p.startDate && !startTime) setStartTime(`${p.startDate}T00:00`);
        if (p.endDate && !endTime && !endType) setEndTime(`${p.endDate}T23:59`);
        if (!p.endDate && p.endType && !endTime) setEndType(p.endType);
      }
      const current = editorRef.current?.getHtml() ?? '';
      if (p.summary.length > 0 && current.replace(/<[^>]*>/g, '').trim() === '') {
        const esc = (s: string) => s.replace(/[&<>"]/g, (c) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;' })[c]!);
        editorRef.current?.setHtml(`<ul>${p.summary.map((s) => `<li>${esc(s)}</li>`).join('')}</ul><p>자세한 내용: ${esc(p.link)}</p>`);
      }
      toast(`AI가 채웠어요. 내용을 확인해 주세요(오늘 ${p.remainingToday}번 남음)`);
    } catch (e) {
      toast((e as Error).message, 'error');
    } finally {
      setFilling(false);
    }
  };
  const [error, setError] = useState<string | null>(null);
  const editorRef = useRef<RichEditorHandle>(null);
  const creating = useRef<Promise<number> | null>(null);

  // 쓸 수 있는 게시판(2026-11-20: 옛 34개 카테고리 대신 게시판 4개. 이벤트 분야는 AI 가 자동으로 정한다)
  const options: readonly string[] = isAdmin ? BOARDS : MEMBER_CATEGORIES;
  const optionValue = (label: string) => label;

  const isEvent = isEventCategory(categories.data, categoryName);
  const isHotDeal = categoryName === '핫딜';

  // 수정: 기존 글 불러오기
  const existing = useQuery({
    queryKey: ['post', Number(editId), 'edit'],
    queryFn: () => postsApi.detail(Number(editId), getViewerId()),
    enabled: editing,
  });
  useEffect(() => {
    if (!existing.data) return;
    fill(existing.data);
    setLoaded(true);
  }, [existing.data]);

  // 새 글: 이어 쓸 임시 저장 글이 있는지 확인
  useEffect(() => {
    if (editing || !isLoggedIn) return;
    let cancelled = false;
    (async () => {
      try {
        const temp = await writeApi.temp();
        if (cancelled || !temp?.postId) return;
        if (confirm('임시 저장된 글이 있어요. 이어서 쓸까요?\n취소하면 임시 저장된 글을 지워요.')) {
          const detail = await postsApi.detail(temp.postId, getViewerId());
          if (cancelled) return;
          setPostId(temp.postId);
          fill(detail);
          editorRef.current?.setHtml(detail.content || '');
        } else {
          await writeApi.deleteTemp();
        }
      } catch {
        /* 임시 글 확인 실패는 새 글 쓰기에 지장 없다 */
      }
    })();
    return () => {
      cancelled = true;
    };
  }, [editing, isLoggedIn]);

  function fill(p: PostDetail) {
    // 옛 세부 카테고리(피자·스킨케어 …) 글은 이벤트 게시판으로. 저장하면 AI 가 분야를 다시 정한다
    setCategoryName(LEGACY_CATEGORY_TO_DOMAIN[p.categoryName] ? '이벤트' : p.categoryName);
    setTitle(p.title ?? '');
    setInitialHtml(p.content ?? '');
    setLink(p.link ?? '');
    setStartTime(toLocalInput(p.startTime));
    setEndTime(toLocalInput(p.endTime));
    setEndType(p.endType ?? '');
    setShoppingMall(p.shoppingMall ?? '');
    setProductName(p.productName ?? '');
    setPrice(p.price ? String(p.price) : '');
    setImages(p.images ?? []);
  }

  const form = (): PostForm => ({
    categoryName,
    title: title.trim(),
    content: editorRef.current?.getHtml() ?? '',
    link: link.trim() || null,
    startTime: isEvent ? fromLocalInput(startTime) : null,
    endTime: isEvent && !endType ? fromLocalInput(endTime) : null,
    endType: isEvent ? endType || null : null,
    shoppingMall: isHotDeal ? shoppingMall.trim() || null : null,
    productName: isHotDeal ? productName.trim() || null : null,
    price: isHotDeal && price ? Number(price.replace(/[^0-9]/g, '')) : null,
  });

  /** 이미지는 게시물 번호가 있어야 올릴 수 있어서, 처음 올릴 때 임시 저장 글을 만든다 */
  const ensurePost = async (): Promise<number> => {
    if (postId) return postId;
    if (!categoryName) throw new Error('카테고리를 먼저 골라 주세요.');
    if (!creating.current) {
      creating.current = writeApi.create({ ...form(), title: form().title || '(제목 없음)' }, true).then((res) => {
        setPostId(res.data.postId);
        return res.data.postId;
      });
    }
    try {
      return await creating.current;
    } catch (e) {
      creating.current = null;
      throw e;
    }
  };

  const totalSize = images.reduce((sum, img) => sum + (img.size || 0), 0);

  const upload = async (file: File, opts: { thumbnail?: boolean; pasted?: boolean } = {}) => {
    if (totalSize + file.size > MAX_TOTAL) throw new Error('이미지는 글 하나에 모두 20MB까지 올릴 수 있어요.');
    const id = await ensurePost();
    const list = await writeApi.uploadImage(id, categoryName, file, opts);
    setImages(list);
    return list;
  };

  const uploadPasted = async (file: File) => {
    try {
      const before = new Set(images.map((i) => i.imageId));
      const list = await upload(file, { pasted: true });
      const added = list.find((i) => !before.has(i.imageId)) ?? list[list.length - 1];
      return added.url;
    } catch (e) {
      toast((e as Error).message, 'error');
      throw e;
    }
  };

  const onFiles = async (files: FileList | null, thumbnail = false) => {
    if (!files?.length) return;
    setBusy(true);
    try {
      for (const file of Array.from(files)) await upload(file, { thumbnail });
    } catch (e) {
      toast((e as Error).message, 'error');
    } finally {
      setBusy(false);
    }
  };

  const removeImage = async (imageId: number) => {
    if (!postId) return;
    try {
      setImages(await writeApi.deleteImages(postId, categoryName, [imageId]));
    } catch (e) {
      toast((e as Error).message, 'error');
    }
  };

  const validate = (): string | null => {
    const f = form();
    if (!f.categoryName) return '카테고리를 골라 주세요.';
    if (!f.title) return '제목을 입력해 주세요.';
    if (f.title.length > 200) return '제목은 200자까지 쓸 수 있어요.';
    if (isEvent) {
      if (!f.link) return '이벤트 링크를 입력해 주세요.';
      if (!startTime) return '시작 일시를 입력해 주세요.';
      if (!endType && !endTime) return '종료 일시를 입력하거나 종료 유형을 골라 주세요.';
      if (!endType && endTime && startTime > endTime) return '종료 일시가 시작 일시보다 빨라요.';
      if (!images.some((i) => i.isThumbnail)) return '이벤트 대표 이미지를 올려 주세요.';
    }
    if (f.link && !/^https?:\/\//.test(f.link)) return '링크는 http:// 또는 https:// 로 시작해야 해요.';
    return null;
  };

  const save = async (isTemp: boolean) => {
    setError(null);
    const message = isTemp ? (form().categoryName ? null : '카테고리를 골라 주세요.') : validate();
    if (message) {
      setError(message);
      window.scrollTo({ top: 0, behavior: 'smooth' });
      return;
    }
    setBusy(true);
    try {
      const f = form();
      let id = postId;
      if (id) {
        const res = await writeApi.update(id, f, isTemp);
        toast(res.message);
      } else {
        const res = await writeApi.create(f, isTemp);
        id = res.data.postId;
        setPostId(id);
        toast(res.message);
      }
      if (!isTemp && id) {
        await refreshAfterPostChange(queryClient, id);
        navigate(`/posts/${id}`, { replace: true });
      }
    } catch (e) {
      setError((e as Error).message);
    } finally {
      setBusy(false);
    }
  };

  if (loading) return null;
  if (!isLoggedIn) return <Navigate to={`/auth/login?redirect=${encodeURIComponent(location.pathname + location.search)}`} replace />;
  if (editing && existing.isError) {
    return (
      <div className="page">
        <div className="container">
          <div className="empty">
            <strong>글을 불러오지 못했어요</strong>
            <Link to="/" className="btn btn-outline">
              홈으로
            </Link>
          </div>
        </div>
      </div>
    );
  }

  const thumbnail = images.find((i) => i.isThumbnail);
  const attachments = images.filter((i) => !i.isPasted && !i.isThumbnail);

  return (
    <div className="page">
      <div className="container" style={{ maxWidth: 900 }}>
        <div className="row-between" style={{ marginBottom: 16 }}>
          <h1 className="page-title">{editing ? '글 수정' : '글쓰기'}</h1>
          <span className="muted">{postId && !editing ? '임시 저장됨' : ''}</span>
        </div>

        <form
          className="panel stack"
          style={{ gap: 18 }}
          onSubmit={(e: FormEvent) => {
            e.preventDefault();
            void save(false);
          }}
        >
          {error && <div className="alert alert-error">{error}</div>}

          <div className="field">
            <label htmlFor="category">카테고리</label>
            <select id="category" className="select" value={categoryName} onChange={(e) => setCategoryName(e.target.value)} disabled={editing && !isAdmin}>
              <option value="">게시판을 고르세요</option>
              {options.map((label) => (
                <option key={label} value={optionValue(label)}>
                  {label}
                </option>
              ))}
            </select>
            {!isAdmin && <span className="help">이벤트·공지 글은 운영진이 올려요. 회원은 자유·핫딜 게시판에 쓸 수 있어요.</span>}
            {isAdmin && categoryName === '이벤트' && <span className="help">✦ 분야(음식·카페, 패션·뷰티 …)와 혜택 유형은 저장 뒤 AI가 3분 안에 자동으로 정해요.</span>}
          </div>

          <div className="field">
            <label htmlFor="title">제목</label>
            <input id="title" className="input" value={title} onChange={(e) => setTitle(e.target.value)} maxLength={200}
              placeholder={isHotDeal ? '[쇼핑몰] 상품명 (가격) (배송비)' : isEvent ? '[브랜드] 이벤트 이름' : '제목을 입력하세요'} />
          </div>

          {isEvent && (
            <div className="form-grid">
              <div className="field">
                <label htmlFor="startTime">시작 일시</label>
                <input id="startTime" type="datetime-local" className="input" value={startTime} onChange={(e) => setStartTime(e.target.value)} />
              </div>
              <div className="field">
                <label htmlFor="endTime">종료 일시</label>
                <input id="endTime" type="datetime-local" className="input" value={endTime} disabled={!!endType}
                  onChange={(e) => setEndTime(e.target.value)} />
              </div>
              <div className="field" style={{ gridColumn: '1 / -1' }}>
                <label>종료 유형(종료일이 정해지지 않은 경우)</label>
                <div className="chips" style={{ flexWrap: 'wrap' }}>
                  {['', ...END_TYPES].map((t) => (
                    <button key={t || 'none'} type="button" className={`chip chip-sm ${endType === t ? 'active' : ''}`} onClick={() => setEndType(t)}>
                      {t || '종료일 있음'}
                    </button>
                  ))}
                </div>
              </div>
            </div>
          )}

          {isHotDeal && (
            <div className="form-grid">
              <div className="field">
                <label htmlFor="mall">쇼핑몰</label>
                <input id="mall" className="input" value={shoppingMall} onChange={(e) => setShoppingMall(e.target.value)} placeholder="예: 쿠팡" />
              </div>
              <div className="field">
                <label htmlFor="price">가격(원)</label>
                <input id="price" className="input" inputMode="numeric" value={price} onChange={(e) => setPrice(e.target.value.replace(/[^0-9]/g, ''))} />
              </div>
              <div className="field" style={{ gridColumn: '1 / -1' }}>
                <label htmlFor="product">상품명</label>
                <input id="product" className="input" value={productName} onChange={(e) => setProductName(e.target.value)} />
              </div>
            </div>
          )}

          {(isEvent || isHotDeal) && (
            <div className="field">
              <label htmlFor="link">{isEvent ? '이벤트 링크' : '구매 링크'}</label>
              <div className="input-row link-row">
                <input id="link" className="input" type="url" value={link} onChange={(e) => setLink(e.target.value)} placeholder="https://" />
                <button type="button" className="btn btn-outline" style={{ height: 48, flexShrink: 0 }} disabled={filling || !/^https?:\/\//.test(link.trim())}
                  onClick={fillFromLink}>
                  {filling ? '읽는 중…' : '✦ 링크로 채우기'}
                </button>
              </div>
              <span className="help">공식 페이지 주소를 넣고 누르면 AI가 페이지를 읽어 제목·기간·요약을 채워 줘요(하루 5번). 채운 내용은 꼭 확인해 주세요.</span>
            </div>
          )}

          {isEvent && (
            <div className="field">
              <label>대표 이미지(목록 썸네일)</label>
              <div className="row-between" style={{ justifyContent: 'flex-start' }}>
                {thumbnail && (
                  <div className="row-thumb" style={{ width: 96, height: 96 }}>
                    <img src={thumbnail.url} alt="대표 이미지" />
                  </div>
                )}
                <label className="btn btn-outline">
                  <Icon name="image" size={18} /> {thumbnail ? '바꾸기' : '올리기'}
                  <input type="file" accept="image/*" hidden onChange={(e) => onFiles(e.target.files, true)} disabled={busy || !categoryName} />
                </label>
              </div>
            </div>
          )}

          <div className="field">
            <label>내용</label>
            {loaded && (
              <Suspense fallback={<div className="skeleton" style={{ height: 480 }} />}>
                <RichEditor ref={editorRef} initialHtml={initialHtml} onUploadImage={uploadPasted} />
              </Suspense>
            )}
          </div>

          <div className="field">
            <div className="row-between">
              <label>첨부 이미지·영상</label>
              <span className="help">
                {formatSize(totalSize)} / 20MB
              </span>
            </div>
            {attachments.length > 0 && (
              <div className="attach-grid">
                {attachments.map((img) => (
                  <div key={img.imageId} className="attach">
                    {img.type === 'video' ? <video src={img.url} muted /> : <img src={img.url} alt={img.originalName} />}
                    <button type="button" className="icon-btn" aria-label="삭제" onClick={() => removeImage(img.imageId)}>
                      <Icon name="close" size={16} />
                    </button>
                  </div>
                ))}
              </div>
            )}
            <label className="btn btn-outline" style={{ alignSelf: 'flex-start' }}>
              <Icon name="image" size={18} /> 파일 추가
              <input type="file" accept="image/*,video/*" multiple hidden onChange={(e) => onFiles(e.target.files)} disabled={busy || !categoryName} />
            </label>
          </div>

          <div className="row-between" style={{ borderTop: '1px solid var(--line)', paddingTop: 16 }}>
            <button type="button" className="btn btn-ghost" onClick={() => navigate(-1)}>
              취소
            </button>
            <div style={{ display: 'flex', gap: 8 }}>
              {!editing && (
                <button type="button" className="btn btn-outline" onClick={() => save(true)} disabled={busy}>
                  임시 저장
                </button>
              )}
              <button className="btn btn-primary" disabled={busy}>
                {busy ? '처리 중…' : editing ? '수정 완료' : '등록'}
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}
