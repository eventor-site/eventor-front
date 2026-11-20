import { Link } from 'react-router';
import type { PostSummary } from '@/api/types';
import { formatDate, formatNumber, splitBracketPrefix, timeAgo } from '@/lib/format';
import { Icon } from './Icon';
import { Thumb } from './Thumb';
import '@/styles/board.css';

/**
 * 게시판별 목록 항목(2026-11-20 개편).
 * - 핫딜: 상품 카드(가격이 주인공)
 * - 커뮤니티: 피드(작성자·미리보기·반응)
 * - 공지: 알림 목록(고정·NEW·날짜)
 */

const DAY = 86_400_000;
const ageDays = (iso: string | null | undefined) => (iso ? (Date.now() - new Date(iso.slice(0, 23)).getTime()) / DAY : 0);

/** 제목에서 가격·무료배송을 뽑는다(가격 칸이 비어 있을 때 대비) */
function parseDeal(title: string) {
  const { prefix, rest } = splitBracketPrefix(title);
  const price = rest.match(/\(([\d,]+)\s*원\)/)?.[1]?.replace(/,/g, '');
  const freeShip = /\((무료|무배|무료배송)\)/.test(rest);
  const name = rest.replace(/\s*\([^)]*\)\s*/g, ' ').trim();
  return { mall: prefix, name, price: price ? Number(price) : null, freeShip };
}

function Reactions({ post }: { post: PostSummary }) {
  return (
    <span className="bd-reactions">
      {!!post.recommendationCount && (
        <span>
          <Icon name="thumb-up" size={13} /> {post.recommendationCount}
        </span>
      )}
      {!!post.commentCount && (
        <span className="bd-comments">
          <Icon name="message" size={13} /> {post.commentCount}
        </span>
      )}
      <span>
        <Icon name="eye" size={13} /> {formatNumber(post.viewCount)}
      </span>
    </span>
  );
}

// ===================================================================== 핫딜

export function HotDealCard({ post }: { post: PostSummary }) {
  const deal = parseDeal(post.title);
  const mall = post.shoppingMall || deal.mall;
  const price = post.price && post.price > 0 ? post.price : deal.price;
  const stale = ageDays(post.createdAt) > 14;
  return (
    <Link to={`/posts/${post.postId}`} className={`bd-deal ${stale ? 'is-stale' : ''}`}>
      <div className="bd-deal-thumb">
        <Thumb src={post.imageUrl} type={post.imageType} alt={post.title} />
        <div className="bd-deal-badges">
          {mall && <span className="bd-chip bd-chip-dark">{mall}</span>}
        </div>
        {deal.freeShip && <span className="bd-chip bd-chip-ship">무료배송</span>}
      </div>
      <div className="bd-deal-body">
        <h3>{post.productName || deal.name}</h3>
        <p className="bd-price">
          {price ? (
            <>
              {price.toLocaleString('ko-KR')}
              <small>원</small>
            </>
          ) : (
            <span className="bd-price-none">가격은 링크에서 확인</span>
          )}
        </p>
        <div className="bd-meta">
          {stale ? <span className="bd-stale" title="올라온 지 2주가 지나 가격·재고가 바뀌었을 수 있어요">지난 특가</span> : <span className="bd-fresh">NEW</span>}
          <span>{timeAgo(post.createdAt)}</span>
          <Reactions post={post} />
        </div>
      </div>
    </Link>
  );
}

// ===================================================================== 커뮤니티

const HUES = [24, 200, 262, 340, 150, 45, 10, 290];
function hue(name: string) {
  let h = 0;
  for (const ch of name) h = (h * 31 + ch.charCodeAt(0)) >>> 0;
  return HUES[h % HUES.length];
}

export function CommunityItem({ post }: { post: PostSummary }) {
  const writer = post.writer || '익명';
  const hot = (post.recommendationCount ?? 0) >= 3 || (post.commentCount ?? 0) >= 5 || (post.viewCount ?? 0) >= 100;
  return (
    <Link to={`/posts/${post.postId}`} className="bd-feed">
      <div className="bd-feed-main">
        <div className="bd-feed-head">
          <span className="bd-avatar" style={{ background: `hsl(${hue(writer)} 70% 55%)` }} aria-hidden="true">
            {writer.replace(/^\[[^\]]*\]\s*/, '').slice(0, 1)}
          </span>
          <span className="bd-writer">{writer}</span>
          {post.writerGrade && <span className="bd-grade">Lv.{post.writerGrade}</span>}
          <span className="bd-dot" aria-hidden="true">·</span>
          <span className="bd-time">{timeAgo(post.createdAt)}</span>
          {hot && <span className="bd-chip bd-chip-hot">🔥 인기</span>}
        </div>
        <h3 className="bd-feed-title">{post.title}</h3>
        {post.excerpt && <p className="bd-excerpt">{post.excerpt}</p>}
        <div className="bd-meta">
          <Reactions post={post} />
        </div>
      </div>
      {post.imageUrl && (
        <div className="bd-feed-thumb">
          <Thumb src={post.imageUrl} type={post.imageType} alt="" />
        </div>
      )}
    </Link>
  );
}

// ===================================================================== 공지

export function NoticeItem({ post }: { post: PostSummary }) {
  const isNew = ageDays(post.createdAt) <= 7;
  return (
    <Link to={`/posts/${post.postId}`} className={`bd-notice ${post.isFixed ? 'is-fixed' : ''}`}>
      <span className="bd-notice-icon" aria-hidden="true">{post.isFixed ? '📌' : '📢'}</span>
      <div className="bd-notice-body">
        <h3>
          {post.isFixed && <span className="bd-chip bd-chip-brand">필독</span>}
          {isNew && <span className="bd-chip bd-chip-new">NEW</span>}
          {post.title}
        </h3>
        {post.excerpt && <p className="bd-excerpt bd-excerpt-1">{post.excerpt}</p>}
      </div>
      <time className="bd-notice-date" dateTime={post.createdAt}>
        {formatDate(post.createdAt)}
      </time>
    </Link>
  );
}

// ===================================================================== 검색 결과(게시판이 섞여 있음)

export function SearchItem({ post }: { post: PostSummary }) {
  const { prefix, rest } = splitBracketPrefix(post.title);
  const deal = post.categoryName === '핫딜' ? parseDeal(post.title) : null;
  const price = post.price && post.price > 0 ? post.price : deal?.price;
  return (
    <Link to={`/posts/${post.postId}`} className="bd-search">
      <div className="bd-search-thumb">
        <Thumb src={post.imageUrl} type={post.imageType} alt="" />
      </div>
      <div className="bd-search-body">
        <div className="bd-search-top">
          <span className="bd-chip bd-chip-soft">{post.categoryName}</span>
          {(post.shoppingMall || prefix) && <span className="bd-search-brand">{post.shoppingMall || prefix}</span>}
        </div>
        <h3>{deal ? deal.name : rest}</h3>
        <div className="bd-meta">
          {price ? <b className="bd-search-price">{price.toLocaleString('ko-KR')}원</b> : post.valueNote ? <b className="bd-search-note">{post.valueNote}</b> : null}
          <span>{timeAgo(post.createdAt)}</span>
          <Reactions post={post} />
        </div>
      </div>
    </Link>
  );
}
