import { useQuery } from '@tanstack/react-query';
import { useEffect, useMemo, useRef, useState, type CSSProperties } from 'react';
import { Link } from 'react-router';
import { bookmarksApi, insightsApi, postsApi, searchApi, tourApi } from '@/api/endpoints';
import type { FestivalItem, MainPost, PostSummary } from '@/api/types';
import { useAuth } from '@/auth/AuthContext';
import { RowSkeletons } from '@/components/Feedback';
import { Icon } from '@/components/Icon';
import { PostRow } from '@/components/PostCard';
import { Thumb } from '@/components/Thumb';
import { categoryHref, useCategories } from '@/lib/useCategories';
import { useDocumentTitle } from '@/lib/useDocumentTitle';
import { formatDate, formatNumber, splitBracketPrefix, timeAgo } from '@/lib/format';
import '@/styles/home.css';

/*
 * 메인 화면(2026-11-20 개편)
 * 원칙: "지금 유효한 정보만". 마감된 이벤트는 메인에 올리지 않고, 모든 목록은 진행 중 이벤트·실시간 축제 기준으로 만든다.
 *  1. 히어로: 스포트라이트 캐러셀(진행 중 인기 이벤트) + 라이브 패널(진행 중 수·축제 수·실시간 인기 검색어)
 *  2. 브랜드 스토리: 진행 중 이벤트가 많은 브랜드
 *  3. 카테고리 바로가기
 *  4. 이번 주말 축제(관광공사 실시간)
 *  5. 마감 다가오는 이벤트(진행률 막대)
 *  6. 핫딜 TOP 10(가격·무료배송 뽑아 보여 줌)
 *  7. 언제든 받는 상시 혜택 / 새로 올라온 이벤트 / 커뮤니티
 */

const DAY = 86_400_000;

function parseDate(v: string | null | undefined): Date | null {
  if (!v) return null;
  const d = new Date(v.length > 23 ? v.slice(0, 23) : v);
  return Number.isNaN(d.getTime()) ? null : d;
}

/** "2026.11.20(금)" → Date */
function parseDotDate(v: string | null | undefined): Date | null {
  const m = v?.match(/(\d{4})\.(\d{2})\.(\d{2})/);
  return m ? new Date(Number(m[1]), Number(m[2]) - 1, Number(m[3])) : null;
}

/** 로컬 날짜 MM.DD */
function md(d: Date) {
  return `${String(d.getMonth() + 1).padStart(2, '0')}.${String(d.getDate()).padStart(2, '0')}`;
}

function daysLeft(end: string | null | undefined): number | null {
  const e = parseDate(end);
  if (!e) return null;
  return Math.floor((e.getTime() - Date.now()) / DAY);
}

function ddayLabel(end: string | null | undefined, endType?: string | null): string {
  const d = daysLeft(end);
  if (d === null) return endType || '상시';
  if (d < 0) return '마감';
  if (d > 365) return '장기 진행';
  return d === 0 ? 'D-DAY' : `D-${d}`;
}

/** 이벤트 기간 중 지난 비율(0~1). 시작일이 없으면 null */
function elapsed(start: string | null | undefined, end: string | null | undefined): number | null {
  const s = parseDate(start);
  const e = parseDate(end);
  if (!s || !e || e <= s) return null;
  return Math.min(1, Math.max(0, (Date.now() - s.getTime()) / (e.getTime() - s.getTime())));
}

/** 핫딜 제목에서 가격·배송 정보를 뽑는다. 예: "[롯데온] 칫솔 30개 (10,800원) (무료)" */
function parseDeal(title: string) {
  const { prefix, rest } = splitBracketPrefix(title);
  const price = rest.match(/\(([\d,]+)\s*원\)/)?.[1] ?? null;
  const freeShip = /\((무료|무배|무료배송)\)/.test(rest);
  const name = rest.replace(/\s*\([^)]*\)\s*/g, ' ').trim();
  return { mall: prefix, name, price, freeShip };
}

function useReducedMotion() {
  const [reduced, setReduced] = useState(false);
  useEffect(() => {
    const mq = window.matchMedia?.('(prefers-reduced-motion: reduce)');
    if (!mq) return;
    setReduced(mq.matches);
    const on = (e: MediaQueryListEvent) => setReduced(e.matches);
    mq.addEventListener('change', on);
    return () => mq.removeEventListener('change', on);
  }, []);
  return reduced;
}

const ongoing = (sort: string, size: number, extra: Record<string, string> = {}) => () =>
  postsApi.search({ categoryName: '이벤트', eventStatusName: '진행중', sort, size, ...extra });

// ===================================================================== 히어로

function Spotlight({ posts }: { posts: PostSummary[] }) {
  const [index, setIndex] = useState(0);
  const [paused, setPaused] = useState(false);
  const reduced = useReducedMotion();
  const trackRef = useRef<HTMLDivElement>(null);
  const count = posts.length;

  // 자동 넘김(5초). 마우스를 올리거나 포커스가 있으면 멈춘다
  useEffect(() => {
    if (paused || reduced || count < 2) return;
    const t = window.setInterval(() => setIndex((i) => (i + 1) % count), 5000);
    return () => window.clearInterval(t);
  }, [paused, reduced, count]);

  useEffect(() => {
    const track = trackRef.current;
    const slide = track?.children[index] as HTMLElement | undefined;
    if (track && slide) track.scrollTo({ left: slide.offsetLeft, behavior: reduced ? 'auto' : 'smooth' });
  }, [index, reduced]);

  // 손가락으로 넘긴 경우 현재 슬라이드 번호를 맞춘다
  const onScroll = () => {
    const track = trackRef.current;
    if (!track) return;
    const i = Math.round(track.scrollLeft / track.clientWidth);
    if (i !== index && i >= 0 && i < count) setIndex(i);
  };

  if (count === 0) return <div className="h-spot h-spot-skeleton skeleton" />;

  return (
    <section
      className="h-spot"
      aria-roledescription="carousel"
      aria-label="지금 주목할 이벤트"
      onMouseEnter={() => setPaused(true)}
      onMouseLeave={() => setPaused(false)}
      onFocus={() => setPaused(true)}
      onBlur={() => setPaused(false)}
    >
      <div className="h-spot-track" ref={trackRef} onScroll={onScroll}>
        {posts.map((p, i) => {
          const { prefix, rest } = splitBracketPrefix(p.title);
          const d = ddayLabel(p.endTime, p.endType);
          return (
            <Link
              key={p.postId}
              to={`/posts/${p.postId}`}
              className="h-spot-slide"
              aria-roledescription="slide"
              aria-label={`${i + 1} / ${count}: ${p.title}`}
              tabIndex={i === index ? 0 : -1}
            >
              {p.imageUrl && <img className="h-spot-bg" src={p.imageUrl} alt="" aria-hidden="true" />}
              <div className="h-spot-media">
                <Thumb src={p.imageUrl} type={p.imageType} alt={p.title} />
              </div>
              <div className="h-spot-copy">
                <div className="h-spot-tags">
                  <span className="h-pill h-pill-live">진행 중</span>
                  {prefix && <span className="h-pill">{prefix}</span>}
                </div>
                <h2 className="h-spot-title">{rest}</h2>
                <p className="h-spot-meta">
                  <span className="h-dday">{d}</span>
                  {p.endTime ? `${formatDate(p.endTime)} 마감` : '기한 없이 진행'}
                  <span aria-hidden="true">·</span>
                  <Icon name="eye" size={14} /> {formatNumber(p.viewCount)}
                </p>
                <span className="h-spot-cta">
                  자세히 보기 <Icon name="chevron-right" size={16} />
                </span>
              </div>
            </Link>
          );
        })}
      </div>
      {count > 1 && (
        <div className="h-spot-nav">
          <button type="button" aria-label="이전" onClick={() => setIndex((index - 1 + count) % count)}>
            <Icon name="chevron-left" size={18} />
          </button>
          <div className="h-spot-dots" role="tablist">
            {posts.map((p, i) => (
              <button
                key={p.postId}
                type="button"
                role="tab"
                aria-selected={i === index}
                aria-label={`${i + 1}번째`}
                className={i === index ? 'active' : ''}
                onClick={() => setIndex(i)}
              >
                {i === index && !paused && !reduced && <span key={index} className="h-spot-progress" />}
              </button>
            ))}
          </div>
          <button type="button" aria-label="다음" onClick={() => setIndex((index + 1) % count)}>
            <Icon name="chevron-right" size={18} />
          </button>
        </div>
      )}
    </section>
  );
}

function KeywordTicker({ keywords }: { keywords: string[] }) {
  const [i, setI] = useState(0);
  // 넓은 화면(히어로 옆 패널)은 목록을 펼쳐 두고, 모바일은 한 줄씩 넘긴다
  const [open, setOpen] = useState(() => window.matchMedia?.('(min-width: 960px)').matches ?? false);
  const reduced = useReducedMotion();
  useEffect(() => {
    if (open || reduced || keywords.length < 2) return;
    const t = window.setInterval(() => setI((x) => (x + 1) % keywords.length), 2600);
    return () => window.clearInterval(t);
  }, [open, reduced, keywords.length]);

  if (keywords.length === 0) return null;
  return (
    <div className={`h-ticker ${open ? 'open' : ''}`}>
      <button type="button" className="h-ticker-head" onClick={() => setOpen((o) => !o)} aria-expanded={open}>
        <span className="h-ticker-label">실시간 인기 검색어</span>
        {!open && (
          <span className="h-ticker-roll" aria-live="off">
            <span key={i} className="h-ticker-item">
              <b>{i + 1}</b> {keywords[i]}
            </span>
          </span>
        )}
        <Icon name="chevron-right" size={16} />
      </button>
      {open && (
        <ol className="h-ticker-list">
          {keywords.map((k, n) => (
            <li key={k}>
              <Link to={`/search?q=${encodeURIComponent(k)}`}>
                <b className={n < 3 ? 'top' : ''}>{n + 1}</b>
                {k}
              </Link>
            </li>
          ))}
        </ol>
      )}
    </div>
  );
}

function LivePanel({ ongoingCount, festivalCount, weekendCount, keywords }: {
  ongoingCount: number | undefined;
  festivalCount: number | undefined;
  weekendCount: number;
  keywords: string[];
}) {
  return (
    <aside className="h-live" aria-label="지금 이벤터">
      <div className="h-live-head">
        <span className="h-live-dot" aria-hidden="true" />
        LIVE · 지금 이벤터
      </div>
      <div className="h-stats">
        <Link to={`${categoryHref('이벤트')}&eventStatusName=진행중`} className="h-stat">
          <span className="h-stat-num">{ongoingCount === undefined ? '–' : formatNumber(ongoingCount)}</span>
          <span className="h-stat-label">진행 중인 이벤트</span>
        </Link>
        <Link to="/tours/festival2" className="h-stat">
          <span className="h-stat-num">{festivalCount === undefined ? '–' : festivalCount}</span>
          <span className="h-stat-label">석 달 안 전국 축제</span>
        </Link>
        <Link to="/tours/festival2" className="h-stat h-stat-wide">
          <span className="h-stat-num">{weekendCount}</span>
          <span className="h-stat-label">이번 주말 열리는 축제</span>
        </Link>
      </div>
      <KeywordTicker keywords={keywords} />
    </aside>
  );
}

// ===================================================================== 브랜드·카테고리

const BRAND_HUES = [24, 340, 262, 200, 150, 45, 10, 290];

function hueOf(name: string) {
  let h = 0;
  for (const ch of name) h = (h * 31 + ch.charCodeAt(0)) >>> 0;
  return BRAND_HUES[h % BRAND_HUES.length];
}

function BrandStories({ posts }: { posts: PostSummary[] }) {
  const brands = useMemo(() => {
    const map = new Map<string, { name: string; count: number; image: string | null }>();
    for (const p of posts) {
      const { prefix } = splitBracketPrefix(p.title);
      if (!prefix || prefix.length > 10) continue;
      const b = map.get(prefix) ?? { name: prefix, count: 0, image: null };
      b.count += 1;
      b.image ??= p.imageUrl;
      map.set(prefix, b);
    }
    return [...map.values()].sort((a, b) => b.count - a.count).slice(0, 14);
  }, [posts]);

  if (brands.length < 3) return null;
  return (
    <section className="h-section" aria-labelledby="h-brands">
      <div className="h-head">
        <h2 id="h-brands" className="h-title">브랜드별 진행 중 이벤트</h2>
      </div>
      <div className="h-stories">
        {brands.map((b) => (
          <Link key={b.name} to={`/brands/${encodeURIComponent(b.name)}`} className="h-story">
            <span className="h-story-ring" style={{ '--h': hueOf(b.name) } as CSSProperties}>
              <span className="h-story-face">
                {b.image ? <img src={b.image} alt="" loading="lazy" /> : b.name.slice(0, 1)}
              </span>
              <span className="h-story-count">{b.count}</span>
            </span>
            <span className="h-story-name">{b.name}</span>
          </Link>
        ))}
      </div>
    </section>
  );
}

const CATEGORY_EMOJI: [RegExp, string][] = [
  [/카페|디저트|커피/, '☕'],
  [/뷰티|스킨|메이크업|화장/, '💄'],
  [/패션|의류|신발|잡화/, '👟'],
  [/피자|치킨|버거|음식|외식|배달|식품/, '🍕'],
  [/예|적금|카드|금융|은행|포인트|페이/, '💳'],
  [/전자|가전|디지털|IT/, '📱'],
  [/공연|콘서트|영화|전시|문화/, '🎤'],
  [/스포츠|운동|아웃도어/, '⚽'],
  [/여행|숙박|항공/, '✈️'],
  [/팝업|굿즈/, '🛍️'],
  [/생활|리빙|홈|라이프/, '🏠'],
  [/엔터|방송|OTT|음악/, '🎬'],
  [/게임/, '🎮'],
  [/시즌|기획/, '🎉'],
];

function emojiFor(name: string) {
  return CATEGORY_EMOJI.find(([re]) => re.test(name))?.[1] ?? '🎁';
}

function CategoryTiles() {
  const categories = useCategories();
  const groups = categories.data?.find((c) => c.name === '이벤트')?.children ?? [];
  if (groups.length === 0) return null;
  return (
    <nav className="h-cats" aria-label="이벤트 카테고리">
      <Link to={categoryHref('이벤트')} className="h-cat">
        <span className="h-cat-emoji">✨</span>전체
      </Link>
      {groups.map((g) => (
        <Link key={g.categoryId} to={categoryHref(g.name)} className="h-cat">
          <span className="h-cat-emoji">{emojiFor(g.name)}</span>
          {g.name}
        </Link>
      ))}
      <Link to={categoryHref('핫딜')} className="h-cat">
        <span className="h-cat-emoji">🔥</span>핫딜
      </Link>
      <Link to="/tours/festival2" className="h-cat">
        <span className="h-cat-emoji">🎪</span>축제
      </Link>
    </nav>
  );
}

// ===================================================================== AI 트렌드

/** 이번 주 리포트 + 분야 비중 미리보기. 전체는 /insights */
function TrendTeaser() {
  const q = useQuery({ queryKey: ['insights', 'overview'], queryFn: insightsApi.overview, staleTime: 5 * 60_000 });
  const o = q.data;
  if (!o) return null;
  const domains = Object.entries(o.byDomain).filter(([, v]) => v > 0).sort((a, b) => b[1] - a[1]).slice(0, 4);
  const benefits = Object.entries(o.byBenefit).filter(([, v]) => v > 0).sort((a, b) => b[1] - a[1]).slice(0, 3);
  if (domains.length === 0) return null;
  const total = domains.reduce((s, [, v]) => s + v, 0) || 1;
  return (
    <section className="h-section" aria-labelledby="h-trend">
      <Link to="/insights" className="h-trend">
        <div className="h-trend-copy">
          <p className="h-eyebrow">✦ AI 혜택 트렌드</p>
          <h2 id="h-trend" className="h-title">{o.report?.headline ?? '지금 어떤 혜택이 많은지 AI가 정리했어요'}</h2>
          <p className="h-trend-sub">
            진행 중 {o.ongoing}개 중 {domains[0][0]} {domains[0][1]}개로 가장 많고, {benefits.map(([k]) => `#${k}`).join(' ')} 혜택이 많아요.
          </p>
        </div>
        <div className="h-trend-bars" aria-hidden="true">
          {domains.map(([k, v]) => (
            <div key={k} className="h-trend-row">
              <span>{k}</span>
              <span className="h-trend-track"><span style={{ width: `${(v / domains[0][1]) * 100}%` }} /></span>
              <b>{Math.round((v / total) * 100)}%</b>
            </div>
          ))}
        </div>
        <span className="h-trend-cta">트렌드 전체 보기 <Icon name="chevron-right" size={16} /></span>
      </Link>
    </section>
  );
}

// ===================================================================== 축제

type FestivalTab = 'weekend' | 'now' | 'soon';

/** 이번 주말(토·일). 일요일이면 오늘 하루 */
function weekendRange(now = new Date()) {
  const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
  const dow = today.getDay();
  const sat = new Date(today.getTime() + (dow === 0 ? -1 : 6 - dow) * DAY);
  const sun = new Date(sat.getTime() + DAY);
  return { from: dow === 0 ? today : sat, to: sun };
}

function festivalOn(f: FestivalItem, from: Date, to: Date) {
  const s = parseDotDate(f.eventStartDate);
  const e = parseDotDate(f.eventEndDate) ?? s;
  return !!s && !!e && s <= to && e >= from;
}

function FestivalShowcase({ festivals }: { festivals: FestivalItem[] }) {
  const { from, to } = weekendRange();
  const lists: Record<FestivalTab, FestivalItem[]> = {
    weekend: festivals.filter((f) => festivalOn(f, from, to)),
    now: festivals.filter((f) => f.eventStatusName === '진행중'),
    soon: festivals.filter((f) => f.eventStatusName === '예정'),
  };
  const firstTab = (['weekend', 'now', 'soon'] as const).find((t) => lists[t].length > 0) ?? 'now';
  const [tab, setTab] = useState<FestivalTab>(firstTab);
  const items = lists[tab].slice(0, 12);
  const tabs: [FestivalTab, string][] = [
    ['weekend', `이번 주말 ${lists.weekend.length}`],
    ['now', `진행 중 ${lists.now.length}`],
    ['soon', `곧 시작 ${lists.soon.length}`],
  ];

  if (festivals.length === 0) return null;
  return (
    <section className="h-section h-fest" aria-labelledby="h-fest">
      <div className="h-head">
        <div>
          <p className="h-eyebrow">🎪 이번 주말 뭐 하지?</p>
          <h2 id="h-fest" className="h-title">
            {md(from)}{from.getTime() !== to.getTime() && ` ~ ${md(to)}`} 전국 축제
          </h2>
        </div>
        <Link to="/tours/festival2" className="h-more">
          전체 축제 <Icon name="chevron-right" size={16} />
        </Link>
      </div>
      <div className="h-tabs" role="tablist">
        {tabs.map(([key, label]) => (
          <button key={key} type="button" role="tab" aria-selected={tab === key} className={tab === key ? 'active' : ''}
            onClick={() => setTab(key)}>
            {label}
          </button>
        ))}
      </div>
      {items.length === 0 ? (
        <p className="h-empty">해당하는 축제가 없어요.</p>
      ) : (
        <div className="h-rail">
          {items.map((f) => (
            <Link key={f.contentId} to={`/tours/${f.contentId}?contentTypeId=${f.contentTypeId}`} className="h-poster">
              <Thumb src={f.firstImage} alt={f.title} />
              <div className="h-poster-shade" />
              <div className="h-poster-top">
                <span className={`h-pill ${f.eventStatusName === '진행중' ? 'h-pill-live' : ''}`}>
                  {f.eventStatusName === '예정' && f.remainingDay ? `${f.remainingDay}일 후` : f.eventStatusName}
                </span>
              </div>
              <div className="h-poster-copy">
                <h3>{f.title}</h3>
                <p>
                  {f.eventStartDate.slice(5)} ~ {f.eventEndDate.slice(5)}
                </p>
                {f.addr1 && (
                  <p className="h-poster-loc">
                    <Icon name="map" size={13} /> {f.addr1.split(' ').slice(0, 2).join(' ')}
                  </p>
                )}
              </div>
            </Link>
          ))}
        </div>
      )}
      <p className="h-source">정보 제공: 한국관광공사</p>
    </section>
  );
}

// ===================================================================== 이벤트 목록

function DeadlineList({ posts }: { posts: PostSummary[] }) {
  const items = posts.filter((p) => {
    const d = daysLeft(p.endTime);
    return d !== null && d >= 0 && d <= 90;
  }).slice(0, 6);
  if (items.length === 0) return null;
  return (
    <section className="h-section" aria-labelledby="h-deadline">
      <div className="h-head">
        <div>
          <p className="h-eyebrow">⏰ 놓치기 전에</p>
          <h2 id="h-deadline" className="h-title">마감이 다가오는 이벤트</h2>
        </div>
        <Link to={`${categoryHref('이벤트')}&eventStatusName=진행중&sort=endTime,asc`} className="h-more">
          더 보기 <Icon name="chevron-right" size={16} />
        </Link>
      </div>
      <div className="h-deadline">
        {items.map((p) => {
          const { prefix, rest } = splitBracketPrefix(p.title);
          const d = daysLeft(p.endTime)!;
          const ratio = elapsed(p.startTime, p.endTime);
          return (
            <Link key={p.postId} to={`/posts/${p.postId}`} className={`h-dl ${d <= 7 ? 'urgent' : ''}`}>
              <div className="h-dl-thumb">
                <Thumb src={p.imageUrl} type={p.imageType} alt={p.title} />
              </div>
              <div className="h-dl-body">
                <div className="h-dl-top">
                  <span className="h-dl-dday">{d === 0 ? 'D-DAY' : `D-${d}`}</span>
                  {prefix && <span className="h-dl-brand">{prefix}</span>}
                </div>
                <h3>{rest}</h3>
                <div className="h-dl-bar" aria-label={ratio === null ? undefined : `기간의 ${Math.round(ratio * 100)}% 지남`}>
                  <span style={{ width: `${Math.round((ratio ?? 0.5) * 100)}%` }} />
                </div>
                <p className="h-dl-date">{formatDate(p.endTime)} 마감</p>
              </div>
            </Link>
          );
        })}
      </div>
    </section>
  );
}

function HotDealTop10({ deals, loading }: { deals: MainPost[] | undefined; loading: boolean }) {
  const top = (deals ?? []).slice(0, 10);
  if (!loading && top.length === 0) return null;
  return (
    <section className="h-section" aria-labelledby="h-deals">
      <div className="h-head">
        <div>
          <p className="h-eyebrow">💸 많이 본 순</p>
          <h2 id="h-deals" className="h-title">핫딜 TOP 10</h2>
        </div>
        <Link to={categoryHref('핫딜')} className="h-more">
          핫딜 전체 <Icon name="chevron-right" size={16} />
        </Link>
      </div>
      <ol className="h-rank">
        {loading
          ? Array.from({ length: 5 }, (_, i) => <li key={i} className="h-rank-item skeleton" style={{ height: 230 }} />)
          : top.map((p, i) => {
              const deal = parseDeal(p.title);
              return (
                <li key={p.postId} className="h-rank-item">
                  <span className="h-rank-num" aria-hidden="true">{i + 1}</span>
                  <Link to={`/posts/${p.postId}`} className="h-deal" aria-label={`${i + 1}위 ${p.title}`}>
                    <div className="h-deal-thumb">
                      <Thumb src={p.imageUrl} type={p.imageType} alt={p.title} />
                      {deal.freeShip && <span className="h-pill h-pill-ship">무료배송</span>}
                    </div>
                    <div className="h-deal-body">
                      {deal.mall && <span className="h-deal-mall">{deal.mall}</span>}
                      <h3>{deal.name}</h3>
                      {deal.price && (
                        <p className="h-deal-price">
                          {deal.price}
                          <small>원</small>
                        </p>
                      )}
                    </div>
                  </Link>
                </li>
              );
            })}
      </ol>
    </section>
  );
}

function Evergreen({ posts }: { posts: PostSummary[] }) {
  if (posts.length === 0) return null;
  return (
    <section className="h-section" aria-labelledby="h-ever">
      <div className="h-head">
        <div>
          <p className="h-eyebrow">🎁 기한 걱정 없이</p>
          <h2 id="h-ever" className="h-title">언제든 받는 상시 혜택</h2>
        </div>
        <Link to={`${categoryHref('이벤트')}&eventStatusName=진행중&endType=${encodeURIComponent('상시')}`} className="h-more">
          더 보기 <Icon name="chevron-right" size={16} />
        </Link>
      </div>
      <div className="h-bento">
        {posts.slice(0, 5).map((p, i) => {
          const { prefix, rest } = splitBracketPrefix(p.title);
          return (
            <Link key={p.postId} to={`/posts/${p.postId}`} className={`h-tile ${i === 0 ? 'h-tile-big' : ''}`}>
              <Thumb src={p.imageUrl} type={p.imageType} alt={p.title} />
              <div className="h-poster-shade" />
              <div className="h-tile-copy">
                {prefix && <span className="h-pill">{prefix}</span>}
                <h3>{rest}</h3>
              </div>
            </Link>
          );
        })}
      </div>
    </section>
  );
}

function NewArrivals({ posts: all }: { posts: PostSummary[] }) {
  const posts = all.filter((p) => p.imageUrl).slice(0, 8);
  if (posts.length === 0) return null;
  return (
    <section className="h-section" aria-labelledby="h-new">
      <div className="h-head">
        <div>
          <p className="h-eyebrow">✨ 최근 등록</p>
          <h2 id="h-new" className="h-title">새로 올라온 진행 중 이벤트</h2>
        </div>
        <Link to={`${categoryHref('이벤트')}&eventStatusName=진행중`} className="h-more">
          더 보기 <Icon name="chevron-right" size={16} />
        </Link>
      </div>
      <div className="h-rail">
        {posts.map((p) => {
          const { prefix, rest } = splitBracketPrefix(p.title);
          return (
            <Link key={p.postId} to={`/posts/${p.postId}`} className="h-card">
              <div className="h-card-thumb">
                <Thumb src={p.imageUrl} type={p.imageType} alt={p.title} />
                <span className="h-pill h-card-dday">{ddayLabel(p.endTime, p.endType)}</span>
              </div>
              {prefix && <span className="h-card-brand">{prefix}</span>}
              <h3>{rest}</h3>
              <span className="h-card-time">{timeAgo(p.createdAt)}</span>
            </Link>
          );
        })}
      </div>
    </section>
  );
}

// ===================================================================== 화면

export function HomePage() {
  useDocumentTitle(null);
  const { isLoggedIn } = useAuth();

  const opt = { staleTime: 60_000 };
  const popular = useQuery({ queryKey: ['home', 'popular'], queryFn: ongoing('viewCount,desc', 40), ...opt });
  const closing = useQuery({ queryKey: ['home', 'closing'], queryFn: ongoing('endTime,asc', 20), ...opt });
  const fresh = useQuery({ queryKey: ['home', 'fresh'], queryFn: ongoing('createdAt,desc', 12), ...opt });
  const evergreen = useQuery({
    queryKey: ['home', 'evergreen'],
    queryFn: ongoing('viewCount,desc', 5, { endType: '상시' }),
    ...opt,
  });
  const hotDeals = useQuery({ queryKey: ['main', 'hotDeals'], queryFn: postsApi.hotDeals, ...opt });
  const community = useQuery({ queryKey: ['main', 'community'], queryFn: postsApi.community, ...opt });
  const keywords = useQuery({ queryKey: ['keywords', 'top10'], queryFn: () => searchApi.topKeywords(10), ...opt });
  const festivals = useQuery({ queryKey: ['festivals'], queryFn: tourApi.festivals, staleTime: 10 * 60_000 });
  const bookmarks = useQuery({ queryKey: ['bookmarks'], queryFn: bookmarksApi.mine, enabled: isLoggedIn });

  const popularPosts = popular.data?.content ?? [];
  // 스포트라이트: 이미지가 있는 인기 진행 중 이벤트 6개(브랜드가 겹치지 않게)
  const spotlight = useMemo(() => {
    const seen = new Set<string>();
    const picked: PostSummary[] = [];
    for (const p of popularPosts) {
      if (!p.imageUrl) continue;
      const brand = splitBracketPrefix(p.title).prefix ?? String(p.postId);
      if (seen.has(brand)) continue;
      seen.add(brand);
      picked.push(p);
      if (picked.length === 6) break;
    }
    return picked;
  }, [popularPosts]);

  const festivalList = festivals.data ?? [];
  const { from, to } = weekendRange();
  const weekendCount = festivalList.filter((f) => festivalOn(f, from, to)).length;

  return (
    <div className="page h-page">
      <div className="container">
        <header className="h-intro">
          <p className="h-eyebrow">{new Date().getFullYear()}.{md(new Date())} · 오늘의 혜택</p>
          <h1 className="h-display">
            놓치면 아쉬운 혜택,
            <br />
            <span className="h-grad">지금 진행 중인 것만</span> 모았어요
          </h1>
        </header>

        <div className="h-hero">
          <Spotlight posts={spotlight} />
          <LivePanel
            ongoingCount={popular.data?.totalElements}
            festivalCount={festivals.data?.length}
            weekendCount={weekendCount}
            keywords={keywords.data ?? []}
          />
        </div>

        {isLoggedIn && !!bookmarks.data?.length && (
          <div className="h-mine">
            <span>
              <Icon name="bookmark" size={14} /> 내 즐겨찾기
            </span>
            {bookmarks.data.map((b) => (
              <Link key={b.bookmarkId} to={categoryHref(b.categoryName)} className="chip chip-sm">
                {b.categoryName}
              </Link>
            ))}
          </div>
        )}

        <CategoryTiles />
        <BrandStories posts={popularPosts} />
        <TrendTeaser />
        <FestivalShowcase key={festivalList.length} festivals={festivalList} />
        <DeadlineList posts={closing.data?.content ?? []} />
        <HotDealTop10 deals={hotDeals.data} loading={hotDeals.isLoading} />
        <Evergreen posts={evergreen.data?.content ?? []} />
        <NewArrivals posts={fresh.data?.content ?? []} />

        <section className="h-section" aria-labelledby="h-comm">
          <div className="h-head">
            <div>
              <p className="h-eyebrow">💬 커뮤니티</p>
              <h2 id="h-comm" className="h-title">방금 올라온 이야기</h2>
            </div>
            <Link to={categoryHref('자유')} className="h-more">
              커뮤니티 <Icon name="chevron-right" size={16} />
            </Link>
          </div>
          <div className="list h-comm">
            {community.isLoading ? <RowSkeletons count={4} /> : community.data?.slice(0, 6).map((p) => <PostRow key={p.postId} post={p} />)}
          </div>
        </section>
      </div>
    </div>
  );
}
