import { useQuery } from '@tanstack/react-query';
import { Link, useParams } from 'react-router';
import { insightsApi } from '@/api/endpoints';
import { Empty, ErrorBox } from '@/components/Feedback';
import { MiniEventCard } from '@/components/InsightCard';
import { formatDate } from '@/lib/format';
import { useDocumentTitle } from '@/lib/useDocumentTitle';
import '@/styles/insights.css';

/** 브랜드 페이지(/brands/:name). 진행 중 이벤트와 지난 이벤트로 본 이 브랜드의 이벤트 습관 */
export function BrandPage() {
  const { name = '' } = useParams();
  useDocumentTitle(`${name} 이벤트`);
  const q = useQuery({ queryKey: ['brand', name], queryFn: () => insightsApi.brand(name), retry: false });
  const b = q.data;
  const benefits = b ? Object.entries(b.byBenefit).sort((x, y) => y[1] - x[1]) : [];

  return (
    <div className="page">
      <div className="container in-page">
        <nav className="breadcrumb">
          <Link to="/insights">혜택 트렌드</Link>
        </nav>
        {q.isError ? (
          <ErrorBox error={q.error} />
        ) : !b ? (
          <div className="skeleton" style={{ height: 360 }} />
        ) : (
          <>
            <header className="in-head">
              <p className="in-eyebrow">브랜드</p>
              <h1 className="in-title">{b.name} 이벤트</h1>
              <p className="in-sub">
                이벤터에 올라온 {b.name} 이벤트 {b.total}개를 모았어요.
                {benefits.length > 0 && ` 주로 ${benefits.slice(0, 2).map(([k]) => k).join('·')} 이벤트를 해요.`}
              </p>
            </header>

            <div className="in-tiles">
              <div className="in-tile in-tile-main">
                <b>{b.ongoing.length}</b>
                <span>지금 진행 중</span>
              </div>
              <div className="in-tile">
                <b>{b.total}</b>
                <span>지금까지 올라온 이벤트</span>
              </div>
              <div className="in-tile">
                <b>{b.stats.avgDays == null ? '–' : `${Math.round(Number(b.stats.avgDays))}일`}</b>
                <span>이벤트 평균 기간</span>
              </div>
              <div className="in-tile">
                <b>{b.stats.lastAt ? formatDate(b.stats.lastAt) : '–'}</b>
                <span>마지막으로 올라온 날</span>
              </div>
            </div>

            {benefits.length > 0 && (
              <div className="ai-tags" style={{ marginTop: 16 }}>
                {benefits.map(([k, v]) => (
                  <span key={k} className="ai-tag ai-tag-soft">
                    #{k} {v}
                  </span>
                ))}
              </div>
            )}

            <section className="in-section">
              <h2 className="in-h2">진행 중인 이벤트</h2>
              {b.ongoing.length === 0 ? (
                <Empty title="지금 진행 중인 이벤트가 없어요" />
              ) : (
                <div className="ai-mini-grid">
                  {b.ongoing.map((e) => (
                    <MiniEventCard key={e.postId} e={e} />
                  ))}
                </div>
              )}
            </section>

            {b.past.length > 0 && (
              <section className="in-section">
                <h2 className="in-h2">지난 이벤트</h2>
                <ul className="in-past">
                  {b.past.map((p) => (
                    <li key={p.postId}>
                      <Link to={`/posts/${p.postId}`}>{p.title}</Link>
                      <span>
                        {formatDate(p.startTime)}
                        {p.endTime ? ` ~ ${formatDate(p.endTime)}` : ''}
                      </span>
                    </li>
                  ))}
                </ul>
              </section>
            )}
          </>
        )}
      </div>
    </div>
  );
}
