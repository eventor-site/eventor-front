import { Link, useLocation } from 'react-router';
import { Icon } from './Icon';

/** 쪽 번호(1부터). 주소의 page 쿼리를 바꾼다 */
export function Pagination({ page, totalPages }: { page: number; totalPages: number }) {
  const location = useLocation();
  if (totalPages <= 1) return null;

  const href = (p: number) => {
    const params = new URLSearchParams(location.search);
    if (p <= 1) params.delete('page');
    else params.set('page', String(p));
    const qs = params.toString();
    return `${location.pathname}${qs ? `?${qs}` : ''}`;
  };

  const windowSize = 5;
  const start = Math.max(1, Math.min(page - Math.floor(windowSize / 2), totalPages - windowSize + 1));
  const end = Math.min(totalPages, start + windowSize - 1);
  const pages = [];
  for (let p = start; p <= end; p++) pages.push(p);

  return (
    <nav className="pagination" aria-label="페이지">
      <Link to={href(page - 1)} className={page <= 1 ? 'disabled' : ''} aria-label="이전 페이지">
        <Icon name="chevron-left" size={18} />
      </Link>
      {start > 1 && (
        <>
          <Link to={href(1)}>1</Link>
          {start > 2 && <span>…</span>}
        </>
      )}
      {pages.map((p) =>
        p === page ? (
          <span key={p} className="current" aria-current="page">
            {p}
          </span>
        ) : (
          <Link key={p} to={href(p)}>
            {p}
          </Link>
        ),
      )}
      {end < totalPages && (
        <>
          {end < totalPages - 1 && <span>…</span>}
          <Link to={href(totalPages)}>{totalPages}</Link>
        </>
      )}
      <Link to={href(page + 1)} className={page >= totalPages ? 'disabled' : ''} aria-label="다음 페이지">
        <Icon name="chevron-right" size={18} />
      </Link>
    </nav>
  );
}
