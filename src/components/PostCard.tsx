import { Link } from 'react-router';
import type { MainPost, PostSummary } from '@/api/types';
import { dday, formatNumber, splitBracketPrefix, timeAgo } from '@/lib/format';
import { Icon } from './Icon';
import { Thumb } from './Thumb';

type CardPost = MainPost & Partial<Pick<PostSummary, 'categoryName' | 'eventStatusName' | 'endType' | 'valueNote'>>;

function statusBadge(post: CardPost) {
  if (post.eventStatusName === '마감') return <span className="badge badge-dark">마감</span>;
  if (post.eventStatusName === '예정') return <span className="badge badge-blue">오픈 예정</span>;
  const d = dday(post.startTime, post.endTime);
  if (d === '마감') return <span className="badge badge-dark">마감</span>;
  if (d && (d === 'D-DAY' || /^D-[0-3]$/.test(d))) return <span className="badge badge-hot">{d}</span>;
  if (d) return <span className="badge badge-dark">{d}</span>;
  if (post.endType === '상시') return <span className="badge badge-dark">상시</span>;
  return null;
}

/** 이벤트 카드(정사각 썸네일 + 제목) */
export function PostCard({ post, showMeta = true }: { post: CardPost; showMeta?: boolean }) {
  const { prefix, rest } = splitBracketPrefix(post.title);
  return (
    <Link to={`/posts/${post.postId}`} className="card">
      <div className="thumb">
        <Thumb src={post.imageUrl} type={post.imageType} alt={post.title} />
        <div className="thumb-badges">{statusBadge(post)}</div>
      </div>
      <div className="card-body">
        {(prefix || post.categoryName) && <div className="card-kicker">{prefix ?? post.categoryName}</div>}
        <h3 className="card-title">{rest}</h3>
        {post.valueNote && <p className="card-note">✦ {post.valueNote}</p>}
        {showMeta && (post.viewCount !== undefined || post.createdAt) && (
          <div className="card-meta">
            {post.viewCount !== undefined && (
              <span className="meta-item">
                <Icon name="eye" size={13} /> {formatNumber(post.viewCount)}
              </span>
            )}
            {!!post.recommendationCount && (
              <span className="meta-item">
                <Icon name="thumb-up" size={13} /> {post.recommendationCount}
              </span>
            )}
            {post.createdAt && <span>{timeAgo(post.createdAt)}</span>}
          </div>
        )}
      </div>
    </Link>
  );
}

/** 목록 한 줄(핫딜·커뮤니티) */
export function PostRow({ post }: { post: CardPost & Partial<PostSummary> }) {
  const { prefix, rest } = splitBracketPrefix(post.title);
  const mall = post.shoppingMall || prefix;
  return (
    <Link to={`/posts/${post.postId}`} className="row">
      <div className="row-thumb">
        <Thumb src={post.imageUrl} type={post.imageType} alt={post.title} />
      </div>
      <div className="row-body">
        <h3 className="row-title">{mall && post.shoppingMall ? rest : post.title}</h3>
        <div className="row-sub">
          {mall && <span className="badge badge-brand">{mall}</span>}
          {post.categoryName && !mall && <span className="badge">{post.categoryName}</span>}
          {post.viewCount !== undefined && (
            <span className="meta-item">
              <Icon name="eye" size={13} /> {formatNumber(post.viewCount)}
            </span>
          )}
          {!!post.recommendationCount && (
            <span className="meta-item">
              <Icon name="thumb-up" size={13} /> {post.recommendationCount}
            </span>
          )}
          {post.createdAt && <span>{timeAgo(post.createdAt)}</span>}
        </div>
      </div>
    </Link>
  );
}
