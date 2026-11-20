import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { bookmarksApi, postsApi, searchApi } from '@/api/endpoints';
import { useAuth } from '@/auth/AuthContext';
import { useToast } from '@/components/Toast';
import { useQueryClient } from '@tanstack/react-query';
import { CardSkeletons, Empty, ErrorBox, RowSkeletons } from '@/components/Feedback';
import { Icon } from '@/components/Icon';
import { SearchBox } from '@/components/Layout';
import { Pagination } from '@/components/Pagination';
import { PostCard } from '@/components/PostCard';
import { CommunityItem, HotDealCard, NoticeItem, SearchItem } from '@/components/BoardItems';
import { Dropdown, SortIcon } from '@/components/Dropdown';
import { formatNumber } from '@/lib/format';
import { categoryHref, categoryPath, findCategory, isEventCategory, useCategories } from '@/lib/useCategories';
import { useDocumentTitle } from '@/lib/useDocumentTitle';
import { BENEFITS, DOMAINS } from '@/api/types';
import '@/styles/insights.css';

const PAGE_SIZE = 20;
const STATUS_TABS = ['', '진행중', '예정', '마감'] as const;
const SORTS = [
  { value: 'createdAt,desc', label: '최신순' },
  { value: 'viewCount,desc', label: '조회순' },
  { value: 'recommendationCount,desc', label: '추천순' },
];
const EVENT_SORTS = [...SORTS, { value: 'endTime,asc', label: '마감 임박순' }];
const SORT_ICON: Record<string, string> = { 'createdAt,desc': '🆕', 'viewCount,desc': '👀', 'recommendationCount,desc': '👍', 'endTime,asc': '⏰' };

/** 게시판마다 머리말 */
const BOARD_INTRO: Record<string, { emoji: string; desc: string; toolbar: string }> = {
  event: { emoji: '🎁', desc: 'AI가 분야·혜택 유형으로 정리한 이벤트예요.', toolbar: '' },
  deal: { emoji: '🔥', desc: '쇼핑몰 특가를 모았어요. 가격과 재고는 바뀔 수 있어요.', toolbar: '최근 올라온 특가' },
  notice: { emoji: '📢', desc: '이벤터 소식과 안내를 알려 드려요.', toolbar: '이벤터 소식' },
  community: { emoji: '💬', desc: '이벤트 후기, 꿀팁, 잡담 모두 환영해요.', toolbar: '새 이야기' },
  search: { emoji: '🔎', desc: '', toolbar: '' },
};

/** 카테고리 목록(/posts?categoryName=…)과 검색(/search?q=…)을 함께 그린다 */
export function PostListPage({ mode }: { mode: 'category' | 'search' }) {
  const [params] = useSearchParams();
  const navigate = useNavigate();
  const categories = useCategories();

  const categoryName = params.get('categoryName') ?? (mode === 'category' ? '이벤트' : '');
  const keyword = mode === 'search' ? (params.get('q') ?? '').trim() : '';
  const page = Math.max(1, Number(params.get('page')) || 1);
  const eventStatusName = params.get('eventStatusName') ?? '';
  const sort = params.get('sort') ?? 'createdAt,desc';
  const domain = params.get('domain') ?? '';
  const benefit = params.get('benefit') ?? '';

  const { isAdmin, isLoggedIn } = useAuth();
  const canWrite = mode === 'category' && (isAdmin || (isLoggedIn && ['자유', '핫딜'].includes(categoryName)));
  const tree = categories.data;
  const isEvent = mode === 'category' && isEventCategory(tree, categoryName);
  const current = findCategory(tree, categoryName);
  const path = categoryPath(tree, categoryName);
  const parent = path.length > 1 ? path[path.length - 2] : undefined;
  const subChips = current && current.children.length > 0 ? current.children : parent?.name !== '이벤트' && parent ? parent.children : [];
  const chipParent = current && current.children.length > 0 ? current : parent;

  useDocumentTitle(mode === 'search' ? (keyword ? `"${keyword}" 검색` : '검색') : categoryName);

  const enabled = mode === 'category' || keyword.length > 0;
  const query = useQuery({
    queryKey: ['posts', 'search', { mode, categoryName, keyword, page, eventStatusName, sort, domain, benefit }],
    queryFn: ({ signal }) =>
      postsApi.search(
        {
          page,
          size: PAGE_SIZE,
          keyword: keyword || undefined,
          categoryName: mode === 'category' ? categoryName : undefined,
          eventStatusName: isEvent ? eventStatusName : undefined,
          domain: isEvent ? domain || undefined : undefined,
          benefit: isEvent ? benefit || undefined : undefined,
          sort,
        },
        signal,
      ),
    enabled,
    placeholderData: keepPreviousData,
  });
  const topKeywords = useQuery({
    queryKey: ['keywords', 'top'],
    queryFn: () => searchApi.topKeywords(10),
    enabled: mode === 'search' && !keyword,
  });

  const setParam = (key: string, value: string) => {
    const next = new URLSearchParams(params);
    if (value) next.set(key, value);
    else next.delete(key);
    next.delete('page');
    navigate({ search: next.toString() });
  };

  const data = query.data;
  const board = mode === 'search' ? 'search' : isEvent ? 'event' : categoryName === '핫딜' ? 'deal' : categoryName === '공지' ? 'notice' : 'community';
  const intro = BOARD_INTRO[board] ?? BOARD_INTRO.community;
  const sortOptions = (isEvent ? EVENT_SORTS : SORTS).map((o) => ({ ...o, icon: SORT_ICON[o.value] }));
  const activeFilters = [
    domain && { key: 'domain', label: domain },
    benefit && { key: 'benefit', label: `#${benefit}` },
  ].filter(Boolean) as { key: string; label: string }[];

  return (
    <div className="page">
      <div className="container">
        {mode === 'search' ? (
          <div className="stack" style={{ gap: 14 }}>
            <h1 className="page-title">검색</h1>
            <div className="hide-desktop">
              <SearchBox large autoFocus={!keyword} />
            </div>
            {keyword && (
              <p className="muted" style={{ margin: 0 }}>
                <strong style={{ color: 'var(--text)' }}>"{keyword}"</strong> 검색 결과{' '}
                {data && <strong style={{ color: 'var(--brand)' }}>{formatNumber(data.totalElements)}</strong>}건
              </p>
            )}
          </div>
        ) : (
          <>
            <nav className="breadcrumb" aria-label="위치">
              <Link to="/">홈</Link>
              {path.map((c) => (
                <span key={c.categoryId} style={{ display: 'contents' }}>
                  <Icon name="chevron-right" size={14} />
                  <Link to={categoryHref(c.name)}>{c.name}</Link>
                </span>
              ))}
            </nav>
            <header className="bd-hero">
              <div className="bd-hero-title">
                <span className="bd-hero-emoji" aria-hidden="true">{intro.emoji}</span>
                <div>
                  <div className="bd-hero-title" style={{ gap: 6 }}>
                    <h1>{categoryName}</h1>
                    <BookmarkButton categoryName={categoryName} />
                  </div>
                  <p>{intro.desc}</p>
                </div>
              </div>
              <div className="bd-hero-actions">
                {data && (
                  <span className="bd-count">
                    <b>{formatNumber(data.totalElements)}</b>개
                  </span>
                )}
                {canWrite && (
                  <Link to={`/write?categoryName=${encodeURIComponent(categoryName)}`} className="btn btn-primary btn-sm hide-mobile">
                    <Icon name="edit" size={16} /> 글쓰기
                  </Link>
                )}
              </div>
            </header>
            {subChips.length > 0 && chipParent && (
              <div className="chips" style={{ marginTop: 14 }}>
                <Link to={categoryHref(chipParent.name)} className={`chip ${categoryName === chipParent.name ? 'active' : ''}`}>
                  전체
                </Link>
                {subChips.map((c) => (
                  <Link key={c.categoryId} to={categoryHref(c.name)} className={`chip ${categoryName === c.name ? 'active' : ''}`}>
                    {c.name}
                  </Link>
                ))}
              </div>
            )}
          </>
        )}

        {enabled && (
          <div className="bd-toolbar">
            {isEvent ? (
              <div className="bd-segment" role="tablist" aria-label="진행 상태">
                {STATUS_TABS.map((s) => (
                  <button key={s || 'all'} role="tab" aria-selected={eventStatusName === s} onClick={() => setParam('eventStatusName', s)}>
                    {s || '전체'}
                  </button>
                ))}
              </div>
            ) : (
              <span className="bd-count">{intro.toolbar}</span>
            )}
            <div className="bd-hero-actions" style={{ gap: 6, flexWrap: 'wrap', justifyContent: 'flex-end' }}>
              {isEvent && (
                <>
                  <Dropdown label="분야" value={domain} align="end" onChange={(v) => setParam('domain', v)}
                    options={[{ value: '', label: '분야 전체' }, ...DOMAINS.map((d) => ({ value: d as string, label: d }))]} />
                  <Dropdown label="혜택 유형" value={benefit} align="end" onChange={(v) => setParam('benefit', v)}
                    options={[{ value: '', label: '혜택 전체' }, ...BENEFITS.map((b) => ({ value: b as string, label: `#${b}` }))]} />
                </>
              )}
              <Dropdown label="정렬" icon={<SortIcon />} value={sort} options={sortOptions} onChange={(v) => setParam('sort', v)} />
            </div>
          </div>
        )}

        {activeFilters.length > 0 && (
          <div className="bd-active">
            <span>AI 분류 필터</span>
            {activeFilters.map((f) => (
              <button key={f.key} type="button" onClick={() => setParam(f.key, '')} aria-label={`${f.label} 필터 빼기`}>
                {f.label} ✕
              </button>
            ))}
            <button type="button" className="bd-reset" onClick={() => { const next = new URLSearchParams(params); next.delete('domain'); next.delete('benefit'); next.delete('page'); navigate({ search: next.toString() }); }}>
              모두 지우기
            </button>
          </div>
        )}

        {mode === 'search' && !keyword && (
          <section className="panel" style={{ marginTop: 16 }}>
            <h2 className="section-title" style={{ fontSize: 17, marginBottom: 10 }}>
              인기 검색어
            </h2>
            {topKeywords.data?.length ? (
              <ol className="rank-list">
                {topKeywords.data.map((k, i) => (
                  <li key={k}>
                    <Link to={`/search?q=${encodeURIComponent(k)}`}>
                      <span className="rank-num">{i + 1}</span>
                      {k}
                    </Link>
                  </li>
                ))}
              </ol>
            ) : (
              <p className="muted" style={{ margin: 0 }}>
                아직 인기 검색어가 없어요.
              </p>
            )}
          </section>
        )}

        {enabled &&
          (query.isError ? (
            <ErrorBox error={query.error} onRetry={() => query.refetch()} />
          ) : !data ? (
            board === 'event' ? (
              <div className="grid"><CardSkeletons count={8} /></div>
            ) : board === 'deal' ? (
              <div className="bd-deals"><CardSkeletons count={10} /></div>
            ) : (
              <div className="list"><RowSkeletons count={8} /></div>
            )
          ) : data.content.length === 0 ? (
            <Empty title={mode === 'search' ? '검색 결과가 없어요' : activeFilters.length ? '조건에 맞는 이벤트가 없어요' : '아직 글이 없어요'}>
              {mode === 'search' && <span>다른 검색어로 찾아보세요.</span>}
            </Empty>
          ) : (
            <div style={{ opacity: query.isFetching ? 0.6 : 1, transition: 'opacity .15s' }}>
              {board === 'event' ? (
                <div className="grid">{data.content.map((p) => <PostCard key={p.postId} post={p} />)}</div>
              ) : board === 'deal' ? (
                <div className="bd-deals">{data.content.map((p) => <HotDealCard key={p.postId} post={p} />)}</div>
              ) : board === 'notice' ? (
                <div className="bd-notices">{data.content.map((p) => <NoticeItem key={p.postId} post={p} />)}</div>
              ) : board === 'search' ? (
                <div className="bd-results">{data.content.map((p) => <SearchItem key={p.postId} post={p} />)}</div>
              ) : (
                <div className="bd-feeds">{data.content.map((p) => <CommunityItem key={p.postId} post={p} />)}</div>
              )}
              <Pagination page={page} totalPages={data.totalPages} />
            </div>
          ))}

        {canWrite && (
          <Link to={`/write?categoryName=${encodeURIComponent(categoryName)}`} className="bd-fab">
            <Icon name="edit" size={18} /> 글쓰기
          </Link>
        )}
      </div>
    </div>
  );
}

/** 카테고리 즐겨찾기(로그인 회원) */
function BookmarkButton({ categoryName }: { categoryName: string }) {
  const { isLoggedIn } = useAuth();
  const toast = useToast();
  const queryClient = useQueryClient();
  const bookmarks = useQuery({ queryKey: ['bookmarks'], queryFn: bookmarksApi.mine, enabled: isLoggedIn });
  if (!isLoggedIn) return null;
  const mine = bookmarks.data?.find((b) => b.categoryName === categoryName);
  const toggle = async () => {
    try {
      const res = mine ? await bookmarksApi.remove(mine.bookmarkId) : await bookmarksApi.add(categoryName);
      toast(res.message ?? (mine ? '즐겨찾기를 해제했어요.' : '즐겨찾기에 추가했어요.'));
      await queryClient.invalidateQueries({ queryKey: ['bookmarks'] });
    } catch (e) {
      toast((e as Error).message, 'error');
    }
  };
  return (
    <button className="icon-btn" onClick={toggle} aria-pressed={!!mine} aria-label={mine ? '즐겨찾기 해제' : '즐겨찾기 추가'}
      title={mine ? '즐겨찾기 해제' : '즐겨찾기 추가'} style={{ color: mine ? 'var(--brand)' : undefined, fontSize: 22 }}>
      {mine ? '★' : '☆'}
    </button>
  );
}
