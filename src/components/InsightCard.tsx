import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router';
import { insightsApi } from '@/api/endpoints';
import type { MiniEvent } from '@/api/types';
import { formatDate, splitBracketPrefix } from '@/lib/format';
import { Thumb } from './Thumb';
import '@/styles/insights.css';

/**
 * 게시물 상세의 "한눈에 보기" 카드. Gemini 가 공식 페이지를 읽고 만든 결과(저장된 값)만 보여 준다.
 * 화면을 열 때마다 Gemini 를 부르지 않는다.
 */
const DIFFICULTY_HINT: Record<string, string> = {
  쉬움: '클릭·로그인 정도면 끝',
  보통: '구매나 가입이 필요해요',
  어려움: '조건이 많거나 추첨이에요',
};

export function InsightCard({ postId }: { postId: number }) {
  const q = useQuery({ queryKey: ['insight', postId], queryFn: () => insightsApi.post(postId), staleTime: 5 * 60_000 });
  const d = q.data;
  if (!d || !d.analyzed) return null;
  const hasDeep = d.summary.length > 0 || d.target || d.howTo || d.valueNote;

  return (
    <section className="ai-card" aria-label="AI 한눈에 보기">
      <header className="ai-card-head">
        <span className="ai-badge">✦ AI 한눈에 보기</span>
        {d.liveStatus === '종료' && <span className="ai-live ai-live-off">공식 페이지 기준 종료된 것 같아요</span>}
        {d.liveStatus === '진행 중' && <span className="ai-live ai-live-on">진행 중 확인</span>}
      </header>

      {d.summary.length > 0 && (
        <ul className="ai-summary">
          {d.summary.map((s) => (
            <li key={s}>{s}</li>
          ))}
        </ul>
      )}

      {hasDeep && (
        <dl className="ai-facts">
          {d.valueNote && (
            <div>
              <dt>혜택</dt>
              <dd className="ai-strong">{d.valueNote}</dd>
            </div>
          )}
          {d.target && (
            <div>
              <dt>대상</dt>
              <dd>{d.target}</dd>
            </div>
          )}
          {d.howTo && (
            <div>
              <dt>참여 방법</dt>
              <dd>{d.howTo}</dd>
            </div>
          )}
          {d.difficulty && (
            <div>
              <dt>난이도</dt>
              <dd>
                <span className={`ai-diff ai-diff-${d.difficulty}`}>{d.difficulty}</span> {DIFFICULTY_HINT[d.difficulty]}
              </dd>
            </div>
          )}
        </dl>
      )}

      <div className="ai-tags">
        {d.domain && (
          <Link to={`/posts?categoryName=${encodeURIComponent('이벤트')}&domain=${encodeURIComponent(d.domain)}&eventStatusName=${encodeURIComponent('진행중')}`} className="ai-tag">
            {d.domain}
          </Link>
        )}
        {d.benefits.map((b) => (
          <Link key={b} to={`/posts?categoryName=${encodeURIComponent('이벤트')}&benefit=${encodeURIComponent(b)}&eventStatusName=${encodeURIComponent('진행중')}`} className="ai-tag ai-tag-soft">
            #{b}
          </Link>
        ))}
        {d.brand && (
          <Link to={`/brands/${encodeURIComponent(d.brand)}`} className="ai-tag ai-tag-brand">
            {d.brand} 이벤트 모아 보기 →
          </Link>
        )}
      </div>

      <p className="ai-foot">
        {d.checkedAt
          ? `${formatDate(d.checkedAt)}에 공식 페이지를 읽고 정리했어요. 정확한 조건은 원문에서 확인해 주세요.`
          : '제목과 기간으로 분류했어요. 정확한 조건은 원문에서 확인해 주세요.'}
      </p>
    </section>
  );
}

export function MiniEventCard({ e }: { e: MiniEvent }) {
  const { prefix, rest } = splitBracketPrefix(e.title);
  return (
    <Link to={`/posts/${e.postId}`} className="ai-mini">
      <div className="ai-mini-thumb">
        <Thumb src={e.imageUrl ?? null} alt={e.title} />
      </div>
      <div className="ai-mini-body">
        {(e.brand || prefix) && <span className="ai-mini-brand">{e.brand || prefix}</span>}
        <h3>{rest}</h3>
        {e.valueNote ? <span className="ai-mini-note">{e.valueNote}</span> : e.endTime ? <span className="ai-mini-note">{formatDate(e.endTime)} 마감</span> : null}
      </div>
    </Link>
  );
}

export function SimilarEvents({ postId }: { postId: number }) {
  const q = useQuery({ queryKey: ['insight', postId], queryFn: () => insightsApi.post(postId), staleTime: 5 * 60_000 });
  const list = q.data?.similar ?? [];
  if (list.length === 0) return null;
  return (
    <section className="ai-similar">
      <h2 className="section-title" style={{ fontSize: 17 }}>이런 이벤트도 진행 중이에요</h2>
      <div className="ai-mini-grid">
        {list.map((e) => (
          <MiniEventCard key={e.postId} e={e} />
        ))}
      </div>
    </section>
  );
}
