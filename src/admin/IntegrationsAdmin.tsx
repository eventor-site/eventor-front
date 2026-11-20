import { useQuery, useQueryClient } from '@tanstack/react-query';
import { useState, type FormEvent } from 'react';
import { api, request } from '@/api/client';
import { ErrorBox } from '@/components/Feedback';
import { Modal } from '@/components/Modal';
import { useToast } from '@/components/Toast';
import { formatDate } from '@/lib/format';
import '@/styles/integrations.css';

/**
 * 관리자 > 외부 연동.
 * 사이트가 쓰는(또는 쓸 예정·썼던) 외부 API·콘솔을 한 화면에서 보고, 키 설정 여부와 연결 상태를 확인한다.
 * 키 값은 서버가 절대 내려주지 않는다(환경 변수 이름과 설정 여부만).
 */

type Status = '사용 중' | '준비 중' | '종료';
type Eligibility = '개인 가능' | '사업자 필요' | '확인 필요';

type Integration = {
  integrationId: number;
  code: string;
  name: string;
  provider: string;
  category: string;
  purpose: string;
  status: Status;
  eligibility: Eligibility;
  eligibilityNote: string | null;
  icon: string | null;
  siteUrl: string | null;
  consoleUrl: string | null;
  docsUrl: string | null;
  envKeys: { name: string; configured: boolean }[];
  memo: string | null;
  sortOrder: number;
  checkable: boolean;
  lastCheckedAt: string | null;
  lastCheckOk: boolean | null;
  lastCheckMessage: string | null;
  updatedAt: string;
};

const PATH = '/api/admin/integrations';
const STATUS_ORDER: Status[] = ['사용 중', '준비 중', '종료'];

function IntegrationIcon({ item }: { item: Integration }) {
  const [broken, setBroken] = useState(false);
  if (!item.icon || broken) {
    return <span className="ig-icon ig-icon-text">{item.name.slice(0, 1)}</span>;
  }
  return <img className="ig-icon" src={`/integrations/${item.icon}`} alt="" onError={() => setBroken(true)} />;
}

function ExternalLink({ href, label }: { href: string | null; label: string }) {
  if (!href) return null;
  return (
    <a className="btn btn-outline btn-sm" href={href} target="_blank" rel="noopener noreferrer">
      {label} ↗
    </a>
  );
}

function Card({ item, onEdit, reload }: { item: Integration; onEdit: () => void; reload: () => void }) {
  const toast = useToast();
  const [checking, setChecking] = useState(false);
  const missing = item.envKeys.filter((k) => !k.configured);

  const check = async () => {
    setChecking(true);
    try {
      const res = await request<Integration>(`${PATH}/${item.integrationId}/check`, { method: 'POST' });
      toast(res.data.lastCheckOk ? `${item.name}: 정상` : `${item.name}: ${res.data.lastCheckMessage}`, res.data.lastCheckOk ? 'info' : 'error');
      reload();
    } catch (e) {
      toast((e as Error).message, 'error');
    } finally {
      setChecking(false);
    }
  };

  const remove = async () => {
    if (!confirm(`"${item.name}" 연동 정보를 지울까요? (키나 외부 설정은 그대로예요)`)) return;
    try {
      const res = await request<void>(`${PATH}/${item.integrationId}`, { method: 'DELETE' });
      toast(res.message ?? '삭제했어요.');
      reload();
    } catch (e) {
      toast((e as Error).message, 'error');
    }
  };

  return (
    <article className={`ig-card ig-${item.status === '사용 중' ? 'on' : item.status === '준비 중' ? 'ready' : 'off'}`}>
      <header className="ig-head">
        <IntegrationIcon item={item} />
        <div className="ig-title">
          <h3>{item.name}</h3>
          <p>
            {item.provider} · {item.category}
          </p>
        </div>
        <span className={`ig-badge ig-status-${STATUS_ORDER.indexOf(item.status)}`}>{item.status}</span>
      </header>

      <p className="ig-purpose">{item.purpose}</p>

      <div className="ig-tags">
        <span
          className={`ig-badge ${item.eligibility === '개인 가능' ? 'ig-ok' : item.eligibility === '사업자 필요' ? 'ig-bad' : 'ig-warn'}`}
          title={item.eligibilityNote ?? undefined}
        >
          {item.eligibility === '개인 가능' ? '👤 ' : item.eligibility === '사업자 필요' ? '🏢 ' : '❔ '}
          {item.eligibility}
        </span>
        {item.envKeys.map((k) => (
          <code key={k.name} className={`ig-env ${k.configured ? 'ig-ok' : 'ig-bad'}`} title={k.configured ? '설정됨' : '비어 있음'}>
            {k.configured ? '✓' : '✕'} {k.name}
          </code>
        ))}
        {item.envKeys.length === 0 && <span className="ig-env ig-muted">키 없음(콘솔만)</span>}
      </div>

      {item.eligibilityNote && <p className="ig-note">{item.eligibilityNote}</p>}
      {item.memo && <p className="ig-memo">📝 {item.memo}</p>}

      {item.status !== '종료' && missing.length > 0 && (
        <p className="ig-alert">
          {item.status === '사용 중' ? '⚠️ 사용 중인데 ' : ''}
          {missing.map((k) => k.name).join(', ')} 가 비어 있어요. 서버의 application.env(운영은 ENV_FILE)에 넣어 주세요.
        </p>
      )}

      {item.checkable && (
        <div className={`ig-check ${item.lastCheckOk === null ? '' : item.lastCheckOk ? 'ig-ok' : 'ig-bad'}`}>
          <span>
            {item.lastCheckedAt
              ? `${item.lastCheckOk ? '정상' : '실패'} · ${formatDate(item.lastCheckedAt, true)}${item.lastCheckMessage ? ` · ${item.lastCheckMessage}` : ''}`
              : '아직 점검하지 않았어요'}
          </span>
          <button className="btn btn-ghost btn-sm" onClick={check} disabled={checking}>
            {checking ? '점검 중…' : '연결 점검'}
          </button>
        </div>
      )}

      <footer className="ig-actions">
        <ExternalLink href={item.siteUrl} label="사이트" />
        <ExternalLink href={item.consoleUrl} label="키·콘솔" />
        <ExternalLink href={item.docsUrl} label="문서" />
        <span className="ig-spacer" />
        <button className="btn btn-ghost btn-sm" onClick={onEdit}>
          수정
        </button>
        <button className="btn btn-ghost btn-sm" style={{ color: 'var(--hot)' }} onClick={remove}>
          삭제
        </button>
      </footer>
    </article>
  );
}

// ===== 등록·수정 =====

type FormValues = Record<
  'code' | 'name' | 'provider' | 'category' | 'purpose' | 'status' | 'eligibility' | 'eligibilityNote' | 'icon' | 'siteUrl' | 'consoleUrl' | 'docsUrl' | 'envKeys' | 'memo' | 'sortOrder',
  string
>;

const FIELDS: { name: keyof FormValues; label: string; help?: string; type?: 'select' | 'textarea' | 'number'; options?: string[]; required?: boolean }[] = [
  { name: 'code', label: '코드', required: true, help: '영문 소문자·숫자·-. 만든 뒤에는 바꿀 수 없어요(자동 점검과 연결).' },
  { name: 'name', label: '이름', required: true },
  { name: 'provider', label: '제공처', required: true },
  { name: 'category', label: '분류', required: true, help: '예: 공공 데이터, 로그인, 메일, 검색엔진, 제휴, AI' },
  { name: 'purpose', label: '용도', required: true, type: 'textarea' },
  { name: 'status', label: '상태', required: true, type: 'select', options: ['사용 중', '준비 중', '종료'] },
  { name: 'eligibility', label: '이용 자격', required: true, type: 'select', options: ['개인 가능', '사업자 필요', '확인 필요'] },
  { name: 'eligibilityNote', label: '자격 메모', type: 'textarea' },
  { name: 'icon', label: '아이콘 파일', help: 'eventor-front/public/integrations/ 아래 png 이름(예: tourapi.png)' },
  { name: 'siteUrl', label: '사이트 주소' },
  { name: 'consoleUrl', label: '키 발급·관리 주소' },
  { name: 'docsUrl', label: '문서 주소' },
  { name: 'envKeys', label: '환경 변수 이름', help: '쉼표로 구분(예: GOOGLE_CLIENT_ID,GOOGLE_CLIENT_SECRET). 키 값은 여기 넣지 마세요.' },
  { name: 'memo', label: '메모', type: 'textarea' },
  { name: 'sortOrder', label: '정렬 순서', type: 'number' },
];

function toForm(item: Integration | null): FormValues {
  return {
    code: item?.code ?? '',
    name: item?.name ?? '',
    provider: item?.provider ?? '',
    category: item?.category ?? '',
    purpose: item?.purpose ?? '',
    status: item?.status ?? '준비 중',
    eligibility: item?.eligibility ?? '확인 필요',
    eligibilityNote: item?.eligibilityNote ?? '',
    icon: item?.icon ?? '',
    siteUrl: item?.siteUrl ?? '',
    consoleUrl: item?.consoleUrl ?? '',
    docsUrl: item?.docsUrl ?? '',
    envKeys: item?.envKeys.map((k) => k.name).join(',') ?? '',
    memo: item?.memo ?? '',
    sortOrder: String(item?.sortOrder ?? 0),
  };
}

function EditDialog({ item, onClose, onSaved }: { item: Integration | null; onClose: () => void; onSaved: () => void }) {
  const toast = useToast();
  const [values, setValues] = useState<FormValues>(() => toForm(item));
  const [busy, setBusy] = useState(false);

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    setBusy(true);
    const body = { ...values, sortOrder: Number(values.sortOrder) || 0 };
    try {
      const res = item
        ? await request<Integration>(`${PATH}/${item.integrationId}`, { method: 'PUT', body })
        : await request<Integration>(PATH, { method: 'POST', body });
      toast(res.message ?? '저장했어요.');
      onSaved();
    } catch (err) {
      toast((err as Error).message, 'error');
    } finally {
      setBusy(false);
    }
  };

  return (
    <Modal title={item ? `${item.name} 수정` : '연동 추가'} onClose={onClose}>
      <form className="stack ig-form" onSubmit={submit}>
        {FIELDS.map((f) => {
          const id = `ig-${f.name}`;
          const common = {
            id,
            value: values[f.name],
            required: f.required,
            onChange: (e: { target: { value: string } }) => setValues({ ...values, [f.name]: e.target.value }),
          };
          return (
            <div className="field" key={f.name}>
              <label htmlFor={id}>{f.label}</label>
              {f.type === 'select' ? (
                <select className="select" {...common}>
                  {f.options!.map((o) => (
                    <option key={o}>{o}</option>
                  ))}
                </select>
              ) : f.type === 'textarea' ? (
                <textarea className="textarea" rows={2} {...common} />
              ) : (
                <input className="input" type={f.type === 'number' ? 'number' : 'text'} {...common}
                  disabled={f.name === 'code' && !!item} />
              )}
              {f.help && <span className="help">{f.help}</span>}
            </div>
          );
        })}
        <button className="btn btn-primary btn-block" disabled={busy}>
          저장
        </button>
      </form>
    </Modal>
  );
}


// ===== Gemini 사용량·작업 =====

type AiStatus = {
  enabled: boolean;
  jobsEnabled: boolean;
  usage: { model: string; requests: number; rpm: number; rpd: number; cooldownSeconds: number; input_tokens?: number; output_tokens?: number; tool_tokens?: number; errors?: number; rate_limited?: number }[];
  backlog: { events: number; classified: number; classifyPending: number; deepPending: number; summarized: number; endedByAi: number };
  report: { headline?: string; weekStart?: string; model?: string };
};

function AiPanel() {
  const toast = useToast();
  const queryClient = useQueryClient();
  const q = useQuery({ queryKey: ['admin', 'ai'], queryFn: () => api<AiStatus>('/api/admin/ai/status'), refetchInterval: 30_000 });
  const [running, setRunning] = useState<string | null>(null);
  const run = async (task: 'classify' | 'deep' | 'report', requests = 4) => {
    setRunning(task);
    try {
      const res = await request<Record<string, unknown> | null>('/api/admin/ai/run', { method: 'POST', body: { task, requests } });
      const d = res.data;
      toast(task === 'report' ? (d ? '주간 리포트를 만들었어요' : '오늘 몫이 없어 만들지 못했어요')
        : `요청 ${d?.requests ?? 0}번 · ${d?.saved ?? 0}건 저장${d?.skippedNoBudget ? ` · 한도로 ${d.skippedNoBudget}건 다음에` : ''}`);
      void queryClient.invalidateQueries({ queryKey: ['admin', 'ai'] });
    } catch (e) {
      toast((e as Error).message, 'error');
    } finally {
      setRunning(null);
    }
  };
  const s = q.data;
  if (!s) return null;
  const b = s.backlog;
  return (
    <section className="ig-ai">
      <div className="row-between">
        <h3 className="ig-group-title" style={{ margin: 0 }}>✦ Gemini 사용량(오늘, 태평양 시간 기준)</h3>
        <span className="muted" style={{ fontSize: 13 }}>{s.jobsEnabled ? '자동 작업 켜짐(3분마다)' : '자동 작업 꺼짐'}</span>
      </div>
      <div className="ig-ai-models">
        {s.usage.map((u) => (
          <div key={u.model} className="ig-ai-model">
            <div className="row-between">
              <code>{u.model}</code>
              <b>{u.requests} / {u.rpd}</b>
            </div>
            <div className="ig-meter"><span style={{ width: `${Math.min(100, (u.requests / Math.max(1, u.rpd)) * 100)}%` }} /></div>
            <small>
              분당 {u.rpm}회 · 토큰 입력 {(u.input_tokens ?? 0).toLocaleString()} / 출력 {(u.output_tokens ?? 0).toLocaleString()} / URL 읽기 {(u.tool_tokens ?? 0).toLocaleString()}
              {u.errors ? ` · 오류 ${u.errors}` : ''}{u.rate_limited ? ` · 한도 초과 ${u.rate_limited}` : ''}{u.cooldownSeconds ? ` · ${u.cooldownSeconds}초 쉬는 중` : ''}
            </small>
          </div>
        ))}
      </div>
      <div className="ig-summary">
        <div><b>{b.classified}</b><span>분류 완료(전체 {b.events})</span></div>
        <div className={b.classifyPending ? 'ig-warn' : ''}><b>{b.classifyPending}</b><span>분류 대기</span></div>
        <div className={b.deepPending ? 'ig-warn' : ''}><b>{b.deepPending}</b><span>링크 읽기 대기</span></div>
        <div><b>{b.summarized}</b><span>요약 있음</span></div>
        <div><b>{b.endedByAi}</b><span>AI가 종료 확인</span></div>
      </div>
      <div className="ig-actions" style={{ borderTop: 0, paddingTop: 0 }}>
        <button className="btn btn-outline btn-sm" disabled={!!running} onClick={() => run('classify')}>{running === 'classify' ? '분류 중…' : '지금 분류'}</button>
        <button className="btn btn-outline btn-sm" disabled={!!running} onClick={() => run('deep')}>{running === 'deep' ? '읽는 중…' : '지금 링크 읽기'}</button>
        <button className="btn btn-outline btn-sm" disabled={!!running} onClick={() => run('report', 1)}>{running === 'report' ? '작성 중…' : '주간 리포트 다시 만들기'}</button>
        <span className="ig-spacer" />
        {s.report?.headline && <span className="muted" style={{ fontSize: 13 }}>최근 리포트: {s.report.headline}</span>}
      </div>
    </section>
  );
}

// ===== 화면 =====

export function IntegrationsAdmin() {
  const queryClient = useQueryClient();
  const q = useQuery({ queryKey: ['admin', 'integrations'], queryFn: () => api<Integration[]>(PATH) });
  const [filter, setFilter] = useState<Status | '전체'>('전체');
  const [editing, setEditing] = useState<Integration | 'new' | null>(null);
  const reload = () => void queryClient.invalidateQueries({ queryKey: ['admin', 'integrations'] });

  const items = q.data ?? [];
  const count = (s: Status) => items.filter((i) => i.status === s).length;
  const missingKeys = items.filter((i) => i.status !== '종료' && i.envKeys.some((k) => !k.configured)).length;
  const failing = items.filter((i) => i.status === '사용 중' && i.lastCheckOk === false).length;
  const needBusiness = items.filter((i) => i.status !== '종료' && i.eligibility === '사업자 필요').length;
  const shown = (s: Status) => items.filter((i) => i.status === s && (filter === '전체' || filter === s));

  return (
    <section className="stack" style={{ gap: 16 }}>
      <div className="row-between">
        <div>
          <h2 className="section-title">외부 연동</h2>
          <p className="section-desc">
            사이트가 쓰는 외부 API·콘솔 목록이에요. 키 값은 여기 보이지 않고, 서버 환경 변수에 설정됐는지만 보여요.
          </p>
        </div>
        <button className="btn btn-primary btn-sm" onClick={() => setEditing('new')}>
          + 추가
        </button>
      </div>

      <AiPanel />

      <div className="ig-summary">
        <div><b>{count('사용 중')}</b><span>사용 중</span></div>
        <div><b>{count('준비 중')}</b><span>준비 중</span></div>
        <div className={missingKeys ? 'ig-warn' : ''}><b>{missingKeys}</b><span>키 미설정</span></div>
        <div className={failing ? 'ig-bad' : ''}><b>{failing}</b><span>점검 실패</span></div>
        <div className={needBusiness ? 'ig-bad' : ''}><b>{needBusiness}</b><span>사업자 필요</span></div>
      </div>

      <div className="chips">
        {(['전체', ...STATUS_ORDER] as const).map((s) => (
          <button key={s} className={`chip chip-sm ${filter === s ? 'active' : ''}`} onClick={() => setFilter(s)}>
            {s}
          </button>
        ))}
      </div>

      {q.isError ? (
        <ErrorBox error={q.error} onRetry={() => q.refetch()} />
      ) : !q.data ? (
        <div className="skeleton" style={{ height: 300 }} />
      ) : (
        STATUS_ORDER.map((s) => {
          const list = shown(s);
          if (list.length === 0) return null;
          const grid = (
            <div className="ig-grid">
              {list.map((item) => (
                <Card key={item.integrationId} item={item} onEdit={() => setEditing(item)} reload={reload} />
              ))}
            </div>
          );
          return s === '종료' && filter !== '종료' ? (
            <details key={s} className="ig-group">
              <summary>종료된 연동 {list.length}개</summary>
              {grid}
            </details>
          ) : (
            <div key={s} className="ig-group">
              <h3 className="ig-group-title">{s} {list.length}</h3>
              {grid}
            </div>
          );
        })
      )}

      {editing && (
        <EditDialog
          item={editing === 'new' ? null : editing}
          onClose={() => setEditing(null)}
          onSaved={() => {
            setEditing(null);
            reload();
          }}
        />
      )}
    </section>
  );
}
