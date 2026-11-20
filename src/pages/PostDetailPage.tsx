import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useMemo, useState, type FormEvent } from 'react';
import { Link, useLocation, useNavigate, useParams } from 'react-router';
import { ApiError } from '@/api/client';
import { commentsApi, postsApi } from '@/api/endpoints';
import type { Comment, PostDetail } from '@/api/types';
import { useAuth } from '@/auth/AuthContext';
import { ErrorBox } from '@/components/Feedback';
import { Icon } from '@/components/Icon';
import { ReportDialog } from '@/components/ReportDialog';
import { useToast } from '@/components/Toast';
import { dday, formatDate, formatNumber, formatPeriod, formatPrice, timeAgo } from '@/lib/format';
import { sanitizeHtml } from '@/lib/sanitize';
import { categoryHref, categoryPath, useCategories } from '@/lib/useCategories';
import { useDocumentTitle } from '@/lib/useDocumentTitle';
import { getViewerId } from '@/lib/viewer';
import { InsightCard, SimilarEvents } from '@/components/InsightCard';

function useLoginGuard() {
  const { isLoggedIn } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const toast = useToast();
  return () => {
    if (isLoggedIn) return true;
    toast('로그인이 필요해요.');
    navigate(`/auth/login?redirect=${encodeURIComponent(location.pathname)}`);
    return false;
  };
}

function InfoCard({ post }: { post: PostDetail }) {
  const isHotDeal = !!(post.shoppingMall || post.productName || post.price);
  const isEvent = !!(post.startTime || post.endTime || post.endType);
  const d = dday(post.startTime, post.endTime);
  if (!isHotDeal && !isEvent && !post.link) return null;
  return (
    <div className="panel info-card">
      {isEvent && (
        <>
          <div className="row-between">
            <strong>이벤트 기간</strong>
            {d && <span className={`badge ${d === '마감' ? '' : 'badge-brand'}`}>{d}</span>}
          </div>
          <div style={{ fontWeight: 700 }}>{formatPeriod(post.startTime, post.endTime, post.endType)}</div>
        </>
      )}
      {isHotDeal && (
        <dl className="stack" style={{ gap: 8, margin: 0 }}>
          {post.shoppingMall && (
            <div className="info-row">
              <dt>쇼핑몰</dt>
              <dd>{post.shoppingMall}</dd>
            </div>
          )}
          {post.productName && (
            <div className="info-row">
              <dt>상품</dt>
              <dd>{post.productName}</dd>
            </div>
          )}
          {!!post.price && (
            <div className="info-row">
              <dt>가격</dt>
              <dd className="price">{formatPrice(post.price)}</dd>
            </div>
          )}
        </dl>
      )}
      {post.link && (
        <a href={post.link} target="_blank" rel="noopener noreferrer nofollow" className="btn btn-primary btn-lg btn-block">
          <Icon name="link" size={18} /> {isHotDeal ? '구매하러 가기' : '이벤트 바로가기'}
        </a>
      )}
    </div>
  );
}

function CommentItem({ comment, postId, onReply }: { comment: Comment; postId: number; onReply: (c: Comment) => void }) {
  const toast = useToast();
  const queryClient = useQueryClient();
  const guard = useLoginGuard();
  const [editing, setEditing] = useState(false);
  const [text, setText] = useState(comment.content);
  const [reporting, setReporting] = useState(false);
  const deleted = !!comment.deletedAt;

  const refresh = () => queryClient.invalidateQueries({ queryKey: ['comments', postId] });
  const act = async (fn: () => Promise<{ message: string | null }>) => {
    try {
      const res = await fn();
      toast(res.message);
      await refresh();
    } catch (e) {
      toast((e as Error).message, 'error');
    }
  };

  return (
    <div className={`comment ${comment.depth > 0 ? 'reply' : ''}`}>
      <div className="comment-head">
        <span className="writer">
          {comment.writerGrade && <span className="grade">{comment.writerGrade}</span>}
          {comment.writer}
        </span>
        <span>{timeAgo(comment.createdAt)}</span>
      </div>
      {editing ? (
        <form
          className="stack"
          style={{ gap: 8 }}
          onSubmit={async (e) => {
            e.preventDefault();
            await act(() => commentsApi.update(comment.commentId, text));
            setEditing(false);
          }}
        >
          <textarea className="textarea" value={text} onChange={(e) => setText(e.target.value)} maxLength={1000} />
          <div className="row-between" style={{ justifyContent: 'flex-end' }}>
            <button type="button" className="btn btn-ghost btn-sm" onClick={() => setEditing(false)}>
              취소
            </button>
            <button className="btn btn-primary btn-sm" disabled={!text.trim()}>
              저장
            </button>
          </div>
        </form>
      ) : (
        <div className="comment-body" style={deleted ? { color: 'var(--text-3)' } : undefined}>
          {comment.parentWriter && !deleted && <strong style={{ color: 'var(--blue)', marginRight: 6 }}>@{comment.parentWriter}</strong>}
          {deleted ? '삭제된 댓글입니다.' : comment.content}
        </div>
      )}
      {reporting && <ReportDialog target="comment" id={comment.commentId} onClose={() => setReporting(false)} />}
      {!deleted && !editing && (
        <div className="comment-actions">
          <button onClick={() => guard() && act(() => commentsApi.recommend(comment.commentId))}>
            <Icon name="thumb-up" size={14} /> {comment.recommendationCount || ''}
          </button>
          <button onClick={() => guard() && act(() => commentsApi.disrecommend(comment.commentId))}>
            <Icon name="thumb-down" size={14} /> {comment.decommendationCount || ''}
          </button>
          <button onClick={() => guard() && onReply(comment)}>
            <Icon name="reply" size={14} /> 답글
          </button>
          {!comment.isAuthorized && <button onClick={() => guard() && setReporting(true)}>신고</button>}
          {comment.isAuthorized && (
            <>
              <button onClick={() => setEditing(true)}>수정</button>
              <button
                onClick={() => {
                  if (confirm('댓글을 삭제할까요?')) void act(() => commentsApi.remove(comment.commentId));
                }}
              >
                삭제
              </button>
            </>
          )}
        </div>
      )}
    </div>
  );
}

function Comments({ postId }: { postId: number }) {
  const { isLoggedIn } = useAuth();
  const location = useLocation();
  const toast = useToast();
  const queryClient = useQueryClient();
  const [text, setText] = useState('');
  const [replyTo, setReplyTo] = useState<Comment | null>(null);
  const { data, isLoading } = useQuery({
    queryKey: ['comments', postId],
    queryFn: () => commentsApi.list(postId, 1, 200),
  });

  const create = useMutation({
    mutationFn: () => commentsApi.create(postId, text.trim(), replyTo?.commentId ?? null),
    onSuccess: async (res) => {
      toast(res.message);
      setText('');
      setReplyTo(null);
      await queryClient.invalidateQueries({ queryKey: ['comments', postId] });
    },
    onError: (e) => toast((e as Error).message, 'error'),
  });

  const submit = (e: FormEvent) => {
    e.preventDefault();
    if (text.trim()) create.mutate();
  };

  return (
    <section className="panel" style={{ marginTop: 20 }} id="comments">
      <h2 className="section-title" style={{ fontSize: 18 }}>
        댓글 <span style={{ color: 'var(--brand)' }}>{data?.totalElements ?? ''}</span>
      </h2>
      {isLoggedIn ? (
        <form onSubmit={submit} className="stack" style={{ gap: 8, marginTop: 14 }}>
          {replyTo && (
            <div className="row-between alert" style={{ padding: '8px 12px' }}>
              <span>
                <strong>@{replyTo.writer}</strong> 님에게 답글
              </span>
              <button type="button" className="btn btn-ghost btn-sm" onClick={() => setReplyTo(null)}>
                취소
              </button>
            </div>
          )}
          <textarea
            className="textarea"
            placeholder="따뜻한 댓글을 남겨 주세요"
            value={text}
            onChange={(e) => setText(e.target.value)}
            maxLength={1000}
          />
          <div className="row-between">
            <span className="help">{text.length}/1000</span>
            <button className="btn btn-primary" disabled={!text.trim() || create.isPending}>
              등록
            </button>
          </div>
        </form>
      ) : (
        <Link
          to={`/auth/login?redirect=${encodeURIComponent(location.pathname)}`}
          className="alert"
          style={{ display: 'block', marginTop: 14, textAlign: 'center' }}
        >
          로그인하고 댓글을 남겨 보세요
        </Link>
      )}
      <div className="comments" style={{ marginTop: 8 }}>
        {isLoading && <p className="muted">불러오는 중…</p>}
        {data?.content.map((c) => (
          <CommentItem
            key={c.commentId}
            comment={c}
            postId={postId}
            onReply={(target) => {
              setReplyTo(target);
              document.getElementById('comments')?.scrollIntoView({ behavior: 'smooth' });
            }}
          />
        ))}
        {data && data.content.length === 0 && <p className="muted">첫 댓글을 남겨 보세요.</p>}
      </div>
    </section>
  );
}

export function PostDetailPage() {
  const { postId: raw } = useParams();
  const postId = Number(raw);
  const navigate = useNavigate();
  const toast = useToast();
  const guard = useLoginGuard();
  const { isLoggedIn } = useAuth();
  const queryClient = useQueryClient();
  const categories = useCategories();
  const [reporting, setReporting] = useState(false);

  const { data: post, error, isLoading, refetch } = useQuery({
    queryKey: ['post', postId],
    queryFn: () => postsApi.detail(postId, getViewerId()),
    enabled: Number.isFinite(postId),
    retry: (count, e) => !(e instanceof ApiError && e.httpStatus === 404) && count < 2,
  });
  const canEdit = useQuery({
    queryKey: ['post', postId, 'canEdit'],
    queryFn: () => postsApi.canEdit(postId),
    enabled: isLoggedIn && !!post,
  });

  useDocumentTitle(post?.title);

  const html = useMemo(() => (post ? sanitizeHtml(post.content || '') : ''), [post]);
  const gallery = useMemo(() => {
    if (!post) return [];
    // 본문에 들어간 이미지는 빼고, 첨부 이미지만 본문 위에 보여 준다.
    // 대표 이미지(썸네일)는 목록용이라 본문에 이미지가 하나라도 있으면 다시 보여 주지 않는다.
    const content = post.content || '';
    const contentHasImage = /<img\s/i.test(content);
    return post.images.filter(
      (img) => !img.isPasted && !content.includes(img.url) && !(img.isThumbnail && contentHasImage),
    );
  }, [post]);

  const act = async (fn: () => Promise<{ message: string | null }>) => {
    if (!guard()) return;
    try {
      const res = await fn();
      toast(res.message);
      await queryClient.invalidateQueries({ queryKey: ['post', postId] });
    } catch (e) {
      toast((e as Error).message, 'error');
    }
  };

  const share = async () => {
    const url = window.location.href;
    try {
      if (navigator.share) await navigator.share({ title: post?.title, url });
      else {
        await navigator.clipboard.writeText(url);
        toast('주소를 복사했어요.');
      }
    } catch {
      /* 사용자가 공유 창을 닫음 */
    }
  };

  if (isLoading) {
    return (
      <div className="page">
        <div className="container">
          <div className="skeleton" style={{ height: 28, width: '70%', marginBottom: 12 }} />
          <div className="skeleton" style={{ height: 14, width: '40%', marginBottom: 24 }} />
          <div className="skeleton" style={{ height: 320 }} />
        </div>
      </div>
    );
  }
  if (error || !post) {
    return (
      <div className="page">
        <div className="container">
          <ErrorBox error={error ?? new Error('게시물을 찾을 수 없어요.')} onRetry={() => refetch()} />
        </div>
      </div>
    );
  }

  const path = categoryPath(categories.data, post.categoryName);

  return (
    <div className="page">
      <div className="container">
        <div className="article">
          <article>
            <nav className="breadcrumb" aria-label="위치">
              <Link to="/">홈</Link>
              {(path.length ? path : [{ categoryId: 0, name: post.categoryName }]).map((c) => (
                <span key={c.name} style={{ display: 'contents' }}>
                  <Icon name="chevron-right" size={14} />
                  <Link to={categoryHref(c.name)}>{c.name}</Link>
                </span>
              ))}
            </nav>
            <h1 className="article-title">{post.title}</h1>
            <div className="article-meta">
              <span className="writer">
                {post.writerGrade && <span className="grade">{post.writerGrade}</span>}
                {post.writer}
              </span>
              <span className="meta-item">
                <Icon name="clock" size={14} /> {formatDate(post.createdAt, true)}
              </span>
              <span className="meta-item">
                <Icon name="eye" size={14} /> {formatNumber(post.viewCount)}
              </span>
              <span className="meta-item">
                <Icon name="thumb-up" size={14} /> {post.recommendationCount}
              </span>
            </div>

            <div className="hide-desktop" style={{ marginTop: 16 }}>
              <InfoCard post={post} />
            </div>
            {post.categoryName !== '핫딜' && <InsightCard postId={postId} />}

            {gallery.length > 0 && (
              <div className="prose" style={{ paddingBottom: 0 }}>
                {gallery.map((img) =>
                  img.type === 'video' ? (
                    <video key={img.imageId} src={img.url} controls playsInline style={{ width: '100%' }} />
                  ) : (
                    <img key={img.imageId} src={img.url} alt={img.originalName} loading="lazy" />
                  ),
                )}
              </div>
            )}
            <div className="prose" dangerouslySetInnerHTML={{ __html: html }} />

            <div className="reactions">
              <button className="reaction" onClick={() => act(() => postsApi.recommend(postId))}>
                <Icon name="thumb-up" size={18} /> 추천 {post.recommendationCount || ''}
              </button>
              <button className="reaction" onClick={() => act(() => postsApi.disrecommend(postId))}>
                <Icon name="thumb-down" size={18} /> 비추천
              </button>
            </div>
            <div className="row-between" style={{ marginTop: 16, flexWrap: 'wrap' }}>
              <div style={{ display: 'flex', gap: 6 }}>
                <button className="btn btn-outline btn-sm" onClick={() => act(() => postsApi.toggleFavorite(postId))}>
                  <Icon name="heart" size={16} /> 찜
                </button>
                <button className="btn btn-outline btn-sm" onClick={share}>
                  <Icon name="share" size={16} /> 공유
                </button>
              </div>
              {!canEdit.data && (
                <button className="btn btn-ghost btn-sm" onClick={() => guard() && setReporting(true)}>
                  신고
                </button>
              )}
              {reporting && <ReportDialog target="post" id={postId} onClose={() => setReporting(false)} />}
              {canEdit.data && (
                <div style={{ display: 'flex', gap: 6 }}>
                  <Link to={`/write/${postId}`} className="btn btn-ghost btn-sm">
                    <Icon name="edit" size={16} /> 수정
                  </Link>
                  <button
                    className="btn btn-ghost btn-sm"
                    onClick={async () => {
                      if (!confirm('게시물을 삭제할까요?')) return;
                      try {
                        const res = await postsApi.remove(postId);
                        toast(res.message);
                        navigate(categoryHref(post.categoryName), { replace: true });
                      } catch (e) {
                        toast((e as Error).message, 'error');
                      }
                    }}
                  >
                    <Icon name="trash" size={16} /> 삭제
                  </button>
                </div>
              )}
            </div>

            <SimilarEvents postId={postId} />
            <Comments postId={postId} />
          </article>

          <aside className="article-aside hide-mobile">
            <InfoCard post={post} />
          </aside>
        </div>
      </div>
    </div>
  );
}
