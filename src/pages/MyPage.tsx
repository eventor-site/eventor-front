import { keepPreviousData, useQuery, useQueryClient } from '@tanstack/react-query';
import { useEffect, useState, type FormEvent } from 'react';
import { Link, Navigate, useNavigate, useParams, useSearchParams } from 'react-router';
import { bookmarksApi, meApi } from '@/api/endpoints';
import type { Me } from '@/api/types';
import { useAuth } from '@/auth/AuthContext';
import { Empty, ErrorBox, RowSkeletons } from '@/components/Feedback';
import { Icon } from '@/components/Icon';
import { Pagination } from '@/components/Pagination';
import { Thumb } from '@/components/Thumb';
import { useToast } from '@/components/Toast';
import { formatDate, formatNumber, timeAgo } from '@/lib/format';
import { categoryHref } from '@/lib/useCategories';
import { useDocumentTitle } from '@/lib/useDocumentTitle';

const TABS = [
  { key: 'posts', label: '내 글' },
  { key: 'comments', label: '내 댓글' },
  { key: 'favorites', label: '찜' },
  { key: 'views', label: '최근 본 글' },
  { key: 'bookmarks', label: '즐겨찾는 카테고리' },
  { key: 'settings', label: '내 정보' },
] as const;
type TabKey = (typeof TABS)[number]['key'];

function usePage() {
  const [params] = useSearchParams();
  return Math.max(1, Number(params.get('page')) || 1);
}

function PagedList<T>({ queryKey, fetcher, render, empty }: {
  queryKey: unknown[];
  fetcher: (page: number) => Promise<import('@/api/types').Page<T>>;
  render: (item: T) => React.ReactNode;
  empty: string;
}) {
  const page = usePage();
  const q = useQuery({ queryKey: [...queryKey, page], queryFn: () => fetcher(page), placeholderData: keepPreviousData });
  if (q.isError) return <ErrorBox error={q.error} onRetry={() => q.refetch()} />;
  if (!q.data) return <div className="list"><RowSkeletons count={5} /></div>;
  if (q.data.content.length === 0) return <Empty title={empty} />;
  return (
    <>
      <div className="list">{q.data.content.map(render)}</div>
      <Pagination page={page} totalPages={q.data.totalPages} />
    </>
  );
}

function Summary({ me }: { me: Me }) {
  return (
    <div className="panel profile">
      <div className="avatar" aria-hidden="true">{me.nickname.slice(0, 1)}</div>
      <div style={{ minWidth: 0 }}>
        <div className="row-between" style={{ justifyContent: 'flex-start', gap: 8 }}>
          <strong style={{ fontSize: 18 }}>{me.nickname}</strong>
          <span className="grade">{me.gradeName}</span>
          {me.roles.includes('admin') && <span className="badge badge-brand">관리자</span>}
        </div>
        <div className="muted" style={{ fontSize: 13 }}>
          {me.oauthType === 'google' ? '구글 계정' : me.oauthType ? `${me.oauthType} 계정` : '아이디 계정'} · 가입 {formatDate(me.createdAt)}
        </div>
      </div>
      <div className="profile-point">
        <span className="muted">포인트</span>
        <strong>{me.point.toLocaleString('ko-KR')}P</strong>
      </div>
    </div>
  );
}

function Settings({ me }: { me: Me }) {
  const toast = useToast();
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const { logout } = useAuth();
  const [nickname, setNickname] = useState(me.nickname);
  const [email, setEmail] = useState(me.email ?? '');
  const [name, setName] = useState(me.name ?? '');
  const [phone, setPhone] = useState(me.phone ?? '');
  const [birth, setBirth] = useState(me.birth ?? '');
  const [gender, setGender] = useState(me.gender ?? '');
  const [currentPw, setCurrentPw] = useState('');
  const [newPw, setNewPw] = useState('');
  const [busy, setBusy] = useState(false);
  const isPasswordAccount = !me.oauthType;

  const saveInfo = async (e: FormEvent) => {
    e.preventDefault();
    setBusy(true);
    try {
      if (nickname !== me.nickname) {
        const check = await meApi.checkNickname(nickname.trim());
        if (check.message && !check.message.startsWith('사용 가능')) {
          toast(check.message, 'error');
          return;
        }
      }
      const res = await meApi.update({
        nickname: nickname.trim(),
        email: email.trim() || null,
        name: name.trim() || null,
        phone: phone.trim() || null,
        birth: birth || null,
        gender: gender || null,
      });
      toast(res.message ?? '저장했어요.');
      await queryClient.invalidateQueries({ queryKey: ['me'] });
    } catch (err) {
      toast((err as Error).message, 'error');
    } finally {
      setBusy(false);
    }
  };

  const changePw = async (e: FormEvent) => {
    e.preventDefault();
    try {
      const res = await meApi.changePassword(currentPw, newPw);
      toast(res.message ?? '비밀번호를 바꿨어요.');
      setCurrentPw('');
      setNewPw('');
    } catch (err) {
      toast((err as Error).message, 'error');
    }
  };

  const withdraw = async () => {
    if (!confirm('정말 탈퇴할까요? 탈퇴하면 같은 계정으로 다시 로그인할 수 없어요.')) return;
    try {
      const res = await meApi.withdraw();
      toast(res.message ?? '탈퇴했어요.');
      await logout();
      navigate('/', { replace: true });
    } catch (err) {
      toast((err as Error).message, 'error');
    }
  };

  return (
    <div className="stack" style={{ gap: 20 }}>
      <form className="panel stack" onSubmit={saveInfo}>
        <h2 className="section-title" style={{ fontSize: 17 }}>기본 정보</h2>
        <div className="form-grid">
          <div className="field">
            <label htmlFor="nickname">닉네임</label>
            <input id="nickname" className="input" value={nickname} onChange={(e) => setNickname(e.target.value)} minLength={2} maxLength={10} required />
            <span className="help">한 달에 한 번 바꿀 수 있어요.{me.nicknameChangedAt ? ` (마지막 변경 ${formatDate(me.nicknameChangedAt)})` : ''}</span>
          </div>
          <div className="field">
            <label htmlFor="email">이메일</label>
            <input id="email" className="input" type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
          </div>
          <div className="field">
            <label htmlFor="name">이름</label>
            <input id="name" className="input" value={name} onChange={(e) => setName(e.target.value)} maxLength={30} />
          </div>
          <div className="field">
            <label htmlFor="phone">연락처</label>
            <input id="phone" className="input" inputMode="tel" value={phone} onChange={(e) => setPhone(e.target.value)} maxLength={15} />
          </div>
          <div className="field">
            <label htmlFor="birth">생년월일</label>
            <input id="birth" className="input" type="date" value={birth} onChange={(e) => setBirth(e.target.value)} />
          </div>
          <div className="field">
            <label htmlFor="gender">성별</label>
            <select id="gender" className="select" value={gender} onChange={(e) => setGender(e.target.value)}>
              <option value="">선택 안 함</option>
              <option value="남">남</option>
              <option value="여">여</option>
            </select>
          </div>
        </div>
        <button className="btn btn-primary" style={{ alignSelf: 'flex-end' }} disabled={busy}>
          저장
        </button>
      </form>

      {isPasswordAccount && (
        <form className="panel stack" onSubmit={changePw}>
          <h2 className="section-title" style={{ fontSize: 17 }}>비밀번호 변경</h2>
          <div className="form-grid">
            <div className="field">
              <label htmlFor="cur">현재 비밀번호</label>
              <input id="cur" className="input" type="password" autoComplete="current-password" value={currentPw} onChange={(e) => setCurrentPw(e.target.value)} required />
            </div>
            <div className="field">
              <label htmlFor="new">새 비밀번호</label>
              <input id="new" className="input" type="password" autoComplete="new-password" value={newPw} onChange={(e) => setNewPw(e.target.value)} required />
              <span className="help">영문·숫자·특수문자를 포함해 8자 이상</span>
            </div>
          </div>
          <button className="btn btn-outline" style={{ alignSelf: 'flex-end' }}>
            비밀번호 변경
          </button>
        </form>
      )}

      <div className="panel row-between">
        <div>
          <strong>회원 탈퇴</strong>
          <div className="help">작성한 글과 댓글은 남아요.</div>
        </div>
        <button className="btn btn-ghost" style={{ color: 'var(--hot)' }} onClick={withdraw}>
          탈퇴하기
        </button>
      </div>
    </div>
  );
}

function Bookmarks() {
  const toast = useToast();
  const queryClient = useQueryClient();
  const q = useQuery({ queryKey: ['bookmarks'], queryFn: bookmarksApi.mine });
  if (!q.data) return <div className="list"><RowSkeletons count={3} /></div>;
  if (q.data.length === 0)
    return (
      <Empty title="즐겨찾는 카테고리가 없어요">
        <span>카테고리 화면의 ☆ 버튼으로 추가할 수 있어요.</span>
      </Empty>
    );
  return (
    <div className="list">
      {q.data.map((b) => (
        <div key={b.bookmarkId} className="row">
          <Link to={categoryHref(b.categoryName)} className="row-body">
            <strong>{b.categoryName}</strong>
          </Link>
          <button
            className="btn btn-ghost btn-sm"
            onClick={async () => {
              const res = await bookmarksApi.remove(b.bookmarkId).catch((e) => toast((e as Error).message, 'error'));
              if (res) toast(res.message ?? '삭제했어요.');
              await queryClient.invalidateQueries({ queryKey: ['bookmarks'] });
            }}
          >
            삭제
          </button>
        </div>
      ))}
    </div>
  );
}

export function MyPage() {
  const { tab = 'posts' } = useParams();
  const { isLoggedIn, loading, isAdmin } = useAuth();
  const toast = useToast();
  const queryClient = useQueryClient();
  const me = useQuery({ queryKey: ['me'], queryFn: meApi.info, enabled: isLoggedIn });
  const active = (TABS.find((t) => t.key === tab)?.key ?? 'posts') as TabKey;
  useDocumentTitle('마이페이지');
  useEffect(() => window.scrollTo({ top: 0 }), [active]);

  if (loading) return null;
  if (!isLoggedIn) return <Navigate to="/auth/login?redirect=/me" replace />;

  return (
    <div className="page">
      <div className="container" style={{ maxWidth: 880 }}>
        <div className="row-between" style={{ marginBottom: 16 }}>
          <h1 className="page-title">마이페이지</h1>
          {isAdmin && (
            <Link to="/admin" className="btn btn-outline btn-sm">
              관리자 화면
            </Link>
          )}
        </div>
        {me.data && <Summary me={me.data} />}
        <nav className="tabs" style={{ margin: '20px 0 16px' }} aria-label="마이페이지 메뉴">
          {TABS.map((t) => (
            <Link key={t.key} to={`/me/${t.key}`} className={active === t.key ? 'active' : ''}>
              {t.label}
            </Link>
          ))}
        </nav>

        {active === 'posts' && (
          <PagedList queryKey={['me', 'posts']} fetcher={meApi.posts} empty="아직 쓴 글이 없어요"
            render={(p) => (
              <div key={p.postId} className="row">
                <Link to={`/posts/${p.postId}`} className="row-body">
                  <h3 className="row-title">{p.title}</h3>
                  <div className="row-sub">
                    <span className="meta-item"><Icon name="eye" size={13} /> {formatNumber(p.viewCount)}</span>
                    <span className="meta-item"><Icon name="thumb-up" size={13} /> {p.recommendationCount}</span>
                    <span>{timeAgo(p.createdAt)}</span>
                  </div>
                </Link>
                <Link to={`/write/${p.postId}`} className="btn btn-ghost btn-sm">
                  수정
                </Link>
              </div>
            )} />
        )}
        {active === 'comments' && (
          <PagedList queryKey={['me', 'comments']} fetcher={meApi.comments} empty="아직 쓴 댓글이 없어요"
            render={(c) => (
              <Link key={c.commentId} to={`/posts/${c.postId}#comments`} className="row">
                <div className="row-body">
                  <div className="row-title" style={{ fontWeight: 500 }}>{c.content}</div>
                  <div className="row-sub">
                    <span className="meta-item"><Icon name="thumb-up" size={13} /> {c.recommendationCount}</span>
                    <span>{timeAgo(c.createdAt)}</span>
                  </div>
                </div>
              </Link>
            )} />
        )}
        {active === 'favorites' && (
          <PagedList queryKey={['me', 'favorites']} fetcher={meApi.favorites} empty="찜한 글이 없어요"
            render={(f) => (
              <div key={f.favoriteId} className="row">
                <Link to={`/posts/${f.postId}`} className="row-body">
                  <h3 className="row-title">{f.title}</h3>
                  <div className="row-sub"><span>{f.writer}</span><span>{timeAgo(f.createdAt)}</span></div>
                </Link>
                <button className="icon-btn" aria-label="찜 해제" style={{ color: 'var(--hot)' }}
                  onClick={async () => {
                    try {
                      const res = await meApi.removeFavorite(f.favoriteId);
                      toast(res.message);
                      await queryClient.invalidateQueries({ queryKey: ['me', 'favorites'] });
                    } catch (e) {
                      toast((e as Error).message, 'error');
                    }
                  }}>
                  <Icon name="heart" />
                </button>
              </div>
            )} />
        )}
        {active === 'views' && (
          <PagedList queryKey={['me', 'views']} fetcher={meApi.postViews} empty="최근 본 글이 없어요"
            render={(v) => (
              <Link key={v.postId} to={`/posts/${v.postId}`} className="row">
                <div className="row-thumb"><Thumb src={v.imageUrl} type={v.imageType} alt={v.title} /></div>
                <div className="row-body"><h3 className="row-title">{v.title}</h3></div>
              </Link>
            )} />
        )}
        {active === 'bookmarks' && <Bookmarks />}
        {active === 'settings' && me.data && <Settings me={me.data} />}
      </div>
    </div>
  );
}
