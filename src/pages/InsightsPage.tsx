import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router';
import { insightsApi } from '@/api/endpoints';
import type { InsightReport } from '@/api/types';
import { ErrorBox } from '@/components/Feedback';
import { Icon } from '@/components/Icon';
import { MiniEventCard } from '@/components/InsightCard';
import { formatDate, formatNumber } from '@/lib/format';
import { useDocumentTitle } from '@/lib/useDocumentTitle';
import '@/styles/insights.css';

/**
 * 혜택 트렌드(/insights). 진행 중 이벤트를 Gemini 로 분류·분석한 결과를 모아 보여 준다.
 * 숫자는 DB에서 센 값이고, 주간 리포트 문장만 Gemini 가 쓴다.
 */

const EVENT_LIST = `/posts?categoryName=${encodeURIComponent('이벤트')}&eventStatusName=${encodeURIComponent('진행중')}`;

/** 한 계열(개수) 가로 막대 목록. 막대는 브랜드 색 하나, 값은 막대 옆에 글자색으로 */
function BarList({ title, data, param, unit = '개' }: { title: string; data: Record<string, number>; param?: 'domain' | 'benefit'; unit?: string }) {
  const rows = Object.entries(data).sort((a, b) => b[1] - a[1]);
  const max = Math.max(1, ...rows.map(([, v]) => v));
  const total = rows.reduce((s, [, v]) => s + v, 0);
  return (
    <section className="in-panel">
      <h2 className="in-h2">{title}</h2>
      <ul className="in-bars">
        {rows.map(([name, value]) => {
          const pct = total ? Math.round((value / total) * 100) : 0;
          const body = (
            <>
              <span className="in-bar-name">{name}</span>
              <span className="in-bar-track">
                <span className="in-bar-fill" style={{ width: `${(value / max) * 100}%` }} />
              </span>
              <span className="in-bar-value">
                {value}
                {unit} <small>{pct}%</small>
              </span>
            </>
          );
          return (
            <li key={name} title={`${name}: ${value}${unit} (${pct}%)`}>
              {param && value > 0 ? (
                <Link to={`${EVENT_LIST}&${param}=${encodeURIComponent(name)}`} className="in-bar">
                  {body}
                </Link>
              ) : (
                <div className="in-bar">{body}</div>
              )}
            </li>
          );
        })}
      </ul>
    </section>
  );
}

function Report({ report }: { report: InsightReport }) {
  let highlights: { postId: number; title: string; reason: string }[] = [];
  try {
    highlights = report.highlights ? JSON.parse(report.highlights) : [];
  } catch {
    highlights = [];
  }
  return (
    <section className="in-report">
      <p className="in-eyebrow">✦ 이번 주 혜택 리포트 · {formatDate(report.weekStart)} 주</p>
      <h2 className="in-report-title">{report.headline}</h2>
      {report.body.split('\n\n').map((p) => (
        <p key={p.slice(0, 20)} className="in-report-body">
          {p}
        </p>
      ))}
      {highlights.length > 0 && (
        <ol className="in-highlights">
          {highlights.map((h) => (
            <li key={h.postId}>
              <Link to={`/posts/${h.postId}`}>
                <b>{h.title}</b>
                <span>{h.reason}</span>
              </Link>
            </li>
          ))}
        </ol>
      )}
      <p className="in-note">통계는 이벤터 데이터 기준이고, 해설은 AI(Gemini)가 그 통계를 보고 썼어요.</p>
    </section>
  );
}

export function InsightsPage() {
  useDocumentTitle('혜택 트렌드');
  const q = useQuery({ queryKey: ['insights', 'overview'], queryFn: insightsApi.overview, staleTime: 5 * 60_000 });
  const o = q.data;
  const difficulty = Object.fromEntries((o?.difficulty ?? []).map((d) => [d.name, d.count]));

  return (
    <div className="page">
      <div className="container in-page">
        <header className="in-head">
          <p className="in-eyebrow">혜택 트렌드</p>
          <h1 className="in-title">지금 진행 중인 혜택, 한눈에</h1>
          <p className="in-sub">이벤터의 진행 중 이벤트를 AI가 분야·혜택 유형·난이도로 정리했어요. 막대를 누르면 해당 이벤트만 볼 수 있어요.</p>
        </header>

        {q.isError ? (
          <ErrorBox error={q.error} onRetry={() => q.refetch()} />
        ) : !o ? (
          <div className="skeleton" style={{ height: 480 }} />
        ) : (
          <>
            <div className="in-tiles">
              <Link to={EVENT_LIST} className="in-tile in-tile-main">
                <b>{formatNumber(o.ongoing)}</b>
                <span>진행 중인 이벤트</span>
              </Link>
              <div className="in-tile">
                <b>{formatNumber(difficulty['쉬움'] ?? 0)}</b>
                <span>클릭 몇 번이면 끝나는 이벤트(AI 판정 "쉬움")</span>
              </div>
              <div className="in-tile">
                <b>{formatNumber(o.newThisWeek)}</b>
                <span>이번 주 새로 올라온 이벤트</span>
              </div>
              <div className="in-tile">
                <b>{formatNumber(o.endedByAi)}</b>
                <span>공식 페이지 확인 결과 종료라 목록에서 뺀 이벤트</span>
              </div>
            </div>

            {o.report && <Report report={o.report} />}

            <div className="in-grid">
              <BarList title="분야별 진행 중 이벤트" data={o.byDomain} param="domain" />
              <BarList title="어떤 혜택이 많을까" data={o.byBenefit} param="benefit" unit="건" />
            </div>

            {o.easyPicks.length > 0 && (
              <section className="in-section">
                <div className="row-between">
                  <h2 className="in-h2">✋ 클릭 몇 번이면 받는 혜택</h2>
                </div>
                <p className="in-sub">AI가 참여 난이도를 "쉬움"으로 본 이벤트 중 많이 본 순이에요.</p>
                <div className="ai-mini-grid">
                  {o.easyPicks.map((e) => (
                    <MiniEventCard key={e.postId} e={e} />
                  ))}
                </div>
              </section>
            )}

            <div className="in-grid">
              {Object.keys(difficulty).length > 0 && (
                <BarList title="참여 난이도(공식 페이지를 읽은 이벤트)" data={{ 쉬움: difficulty['쉬움'] ?? 0, 보통: difficulty['보통'] ?? 0, 어려움: difficulty['어려움'] ?? 0 }} />
              )}
              <section className="in-panel">
                <h2 className="in-h2">진행 중 이벤트가 많은 브랜드</h2>
                <div className="in-brands">
                  {o.topBrands.map((b, i) => (
                    <Link key={b.name} to={`/brands/${encodeURIComponent(b.name)}`} className="in-brand">
                      <span className="in-brand-rank">{i + 1}</span>
                      <span className="in-brand-name">{b.name}</span>
                      <span className="in-brand-count">{b.ongoing}개</span>
                    </Link>
                  ))}
                </div>
              </section>
            </div>

            {o.endingSoon.length > 0 && (
              <section className="in-section">
                <h2 className="in-h2">
                  <Icon name="clock" size={18} /> 2주 안에 끝나요
                </h2>
                <div className="ai-mini-grid">
                  {o.endingSoon.map((e) => (
                    <MiniEventCard key={e.postId} e={e} />
                  ))}
                </div>
              </section>
            )}

            <details className="in-table">
              <summary>표로 보기</summary>
              <table className="table">
                <thead>
                  <tr>
                    <th>구분</th>
                    <th>항목</th>
                    <th>개수</th>
                  </tr>
                </thead>
                <tbody>
                  {Object.entries(o.byDomain).map(([k, v]) => (
                    <tr key={`d-${k}`}>
                      <td>분야</td>
                      <td>{k}</td>
                      <td>{v}</td>
                    </tr>
                  ))}
                  {Object.entries(o.byBenefit).map(([k, v]) => (
                    <tr key={`b-${k}`}>
                      <td>혜택 유형</td>
                      <td>{k}</td>
                      <td>{v}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </details>
          </>
        )}
      </div>
    </div>
  );
}
