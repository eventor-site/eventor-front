import { keepPreviousData, useQuery, useQueryClient } from '@tanstack/react-query';
import { useState } from 'react';
import { Link, Navigate, useParams } from 'react-router';
import { api, request } from '@/api/client';
import type { Page } from '@/api/types';
import { useAuth } from '@/auth/AuthContext';
import { Modal } from '@/components/Modal';
import { useToast } from '@/components/Toast';
import { formatDate, formatNumber } from '@/lib/format';
import { useDocumentTitle } from '@/lib/useDocumentTitle';
import { IntegrationsAdmin } from './IntegrationsAdmin';
import { ResourceManager, type ResourceConfig } from './ResourceManager';

type Row = Record<string, unknown>;

// ===== 설정만으로 만드는 관리 화면 =====
const nameOnly = (title: string, path: string, idKey: string, description?: string): ResourceConfig<Row> => ({
  title,
  path,
  idKey,
  description,
  columns: [
    { key: idKey, label: 'ID', width: 80 },
    { key: 'name', label: '이름' },
  ],
  fields: [{ name: 'name', label: '이름', required: true }],
  canCreate: true,
  canUpdate: true,
  canDelete: true,
});

const RESOURCES: Record<string, ResourceConfig<Row>> = {
  grades: {
    title: '등급',
    path: '/api/grades',
    idKey: 'gradeId',
    description: '포인트 구간별 회원 등급',
    columns: [
      { key: 'gradeId', label: 'ID', width: 80 },
      { key: 'name', label: '등급' },
      { key: 'minAmount', label: '최소 포인트', render: (r) => formatNumber(r.minAmount as number) },
      { key: 'maxAmount', label: '최대 포인트', render: (r) => formatNumber(r.maxAmount as number) },
    ],
    fields: [
      { name: 'name', label: '등급 이름', required: true },
      { name: 'minAmount', label: '최소 포인트', type: 'number', required: true },
      { name: 'maxAmount', label: '최대 포인트', type: 'number', required: true },
    ],
    canCreate: true,
    canUpdate: true,
    canDelete: true,
  },
  points: {
    title: '포인트 정책',
    path: '/api/points',
    idKey: 'pointId',
    description: '활동별 지급·차감 포인트',
    columns: [
      { key: 'pointId', label: 'ID', width: 80 },
      { key: 'name', label: '활동' },
      { key: 'amount', label: '포인트', render: (r) => `${(r.amount as number) > 0 ? '+' : ''}${r.amount}` },
    ],
    fields: [
      { name: 'name', label: '활동 이름', required: true },
      { name: 'amount', label: '포인트(차감은 음수)', type: 'number', required: true },
    ],
    canCreate: true,
    canUpdate: true,
    canDelete: true,
  },
  roles: nameOnly('역할', '/api/roles', 'roleId', 'admin·member 등. 이름을 바꾸면 권한 검사에 영향이 있으니 주의'),
  statusTypes: nameOnly('상태 유형', '/api/statusTypes', 'statusTypeId'),
  reportTypes: nameOnly('신고 사유', '/api/reportTypes', 'reportTypeId', '신고할 때 고르는 사유'),
  statuses: {
    title: '상태',
    path: '/api/statuses',
    idKey: 'statusId',
    columns: [
      { key: 'statusId', label: 'ID', width: 80 },
      { key: 'statusTypeName', label: '유형' },
      { key: 'name', label: '상태' },
    ],
    fields: [
      { name: 'name', label: '상태 이름', required: true },
      { name: 'statusTypeId', label: '상태 유형 ID', type: 'number', required: true, help: '상태 유형 메뉴에서 ID 확인' },
    ],
    canCreate: true,
    canUpdate: true,
    canDelete: true,
  },
  banNicknames: {
    title: '금지 닉네임',
    path: '/api/banNicknames',
    idKey: 'banNicknameId',
    description: '이 단어가 들어간 닉네임은 쓸 수 없어요',
    columns: [
      { key: 'banNicknameId', label: 'ID', width: 80 },
      { key: 'nickname', label: '금지어' },
    ],
    fields: [{ name: 'nickname', label: '금지어', required: true }],
    canCreate: true,
    canDelete: true,
  },
  categories: {
    title: '카테고리',
    path: '/api/categories',
    idKey: 'categoryId',
    description: '상위 카테고리 이름을 비우면 최상위로 만들어요',
    columns: [
      { key: 'categoryId', label: 'ID', width: 80 },
      { key: 'name', label: '이름', render: (r) => `${'— '.repeat(Number(r.depth ?? 0))}${r.name}` },
      { key: 'depth', label: '깊이' },
    ],
    fields: [
      { name: 'name', label: '이름', required: true },
      { name: 'parentCategoryName', label: '상위 카테고리 이름' },
    ],
    canCreate: true,
    canUpdate: true,
    canDelete: true,
    pageSize: 50,
  },
  userStops: {
    title: '이용 정지',
    path: '/api/userStops',
    idKey: 'userStopId',
    columns: [
      { key: 'userStopId', label: 'ID', width: 80 },
      { key: 'nickname', label: '회원' },
      { key: 'reportTypeName', label: '사유' },
      { key: 'stopDay', label: '일수' },
      { key: 'endTime', label: '해제 예정', render: (r) => formatDate(r.endTime as string, true) },
    ],
    fields: [
      { name: 'userId', label: '회원 ID', type: 'number', required: true },
      { name: 'reportTypeId', label: '신고 사유 ID', type: 'number', required: true },
      { name: 'stopDay', label: '정지 일수', type: 'number', required: true },
    ],
    canCreate: true,
    canDelete: true,
  },
  postReports: {
    title: '게시물 신고',
    path: '/api/postReports',
    idKey: 'postReportId',
    columns: [
      { key: 'createdAt', label: '신고일', render: (r) => formatDate(r.createdAt as string, true) },
      { key: 'title', label: '게시물', render: (r) => <Link to={`/posts/${r.postId}`} target="_blank">{String(r.title)}</Link> },
      { key: 'reportTypeName', label: '사유' },
      { key: 'isChecked', label: '확인', render: (r) => (r.isChecked ? '✅' : '대기') },
    ],
    canDelete: true,
    actions: (r, reload) => <ConfirmButton path={`/api/postReports/${r.postReportId}/confirm`} done={!!r.isChecked} reload={reload} />,
  },
  commentReports: {
    title: '댓글 신고',
    path: '/api/commentReports',
    idKey: 'commentReportId',
    columns: [
      { key: 'createdAt', label: '신고일', render: (r) => formatDate(r.createdAt as string, true) },
      { key: 'content', label: '댓글', render: (r) => <Link to={`/posts/${r.postId}#comments`} target="_blank">{String(r.content ?? '')}</Link> },
      { key: 'reportTypeName', label: '사유' },
      { key: 'isChecked', label: '확인', render: (r) => (r.isChecked ? '✅' : '대기') },
    ],
    canDelete: true,
    actions: (r, reload) => <ConfirmButton path={`/api/commentReports/${r.commentReportId}/confirm`} done={!!r.isChecked} reload={reload} />,
  },
};

function ConfirmButton({ path, done, reload }: { path: string; done: boolean; reload: () => void }) {
  const toast = useToast();
  if (done) return null;
  return (
    <button className="btn btn-outline btn-sm" onClick={async () => {
      try {
        const res = await request<void>(path, { method: 'PUT' });
        toast(res.message ?? '확인 처리했어요.');
        reload();
      } catch (e) {
        toast((e as Error).message, 'error');
      }
    }}>
      확인 처리
    </button>
  );
}

// ===== 대시보드 =====
type Stat = { date: string; visitorCount: number; mainViewCount: number; postViewCount: number; loginCount: number; signupCount: number };

function Dashboard() {
  const stats = useQuery({ queryKey: ['admin', 'stats'], queryFn: () => api<Page<Stat>>('/api/statistics', { query: { page: 1, size: 14 } }) });
  const byAdmin = useQuery({
    queryKey: ['admin', 'eventCountByAdmin'],
    queryFn: () => api<{ nickname?: string; writer?: string; count?: number; postCount?: number }[]>('/api/posts/statistic/users/admin'),
  });
  const today = stats.data?.content[0];
  return (
    <section className="stack" style={{ gap: 18 }}>
      <h2 className="section-title">대시보드</h2>
      {today && (
        <div className="stat-cards">
          {[
            ['방문자', today.visitorCount],
            ['메인 조회', today.mainViewCount],
            ['게시물 조회', today.postViewCount],
            ['로그인 회원', today.loginCount],
            ['신규 가입', today.signupCount],
          ].map(([label, value]) => (
            <div className="stat-card" key={label as string}>
              <span>오늘 {label}</span>
              <strong>{formatNumber(value as number)}</strong>
            </div>
          ))}
        </div>
      )}
      <div className="table-wrap">
        <table className="table">
          <thead>
            <tr><th>날짜</th><th>방문자</th><th>메인 조회</th><th>게시물 조회</th><th>로그인</th><th>가입</th></tr>
          </thead>
          <tbody>
            {stats.data?.content.map((s) => (
              <tr key={s.date}>
                <td>{s.date}</td><td>{formatNumber(s.visitorCount)}</td><td>{formatNumber(s.mainViewCount)}</td>
                <td>{formatNumber(s.postViewCount)}</td><td>{s.loginCount}</td><td>{s.signupCount}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {!!byAdmin.data?.length && (
        <>
          <h3 className="section-title" style={{ fontSize: 16 }}>이번 달 운영진 이벤트 등록 수</h3>
          <div className="table-wrap">
            <table className="table">
              <tbody>
                {byAdmin.data.map((r, i) => (
                  <tr key={i}>
                    {Object.values(r).map((v, j) => <td key={j}>{String(v)}</td>)}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}
    </section>
  );
}

// ===== 회원 =====
type UserRow = { userId: number; nickname: string; statusName: string; gradeName: string; roles: string[] };

function Users() {
  const [page, setPage] = useState(1);
  const [selected, setSelected] = useState<UserRow | null>(null);
  const q = useQuery({ queryKey: ['admin', 'users', page], queryFn: () => api<Page<UserRow>>('/api/users', { query: { page, size: 30 } }), placeholderData: keepPreviousData });
  return (
    <section className="stack" style={{ gap: 14 }}>
      <h2 className="section-title">회원 {q.data && <span className="muted" style={{ fontSize: 14 }}>{q.data.totalElements}명</span>}</h2>
      <div className="table-wrap">
        <table className="table">
          <thead><tr><th>ID</th><th>닉네임</th><th>상태</th><th>등급</th><th>역할</th><th /></tr></thead>
          <tbody>
            {q.data?.content.map((u) => (
              <tr key={u.userId}>
                <td>{u.userId}</td><td>{u.nickname}</td><td>{u.statusName}</td><td>{u.gradeName}</td><td>{u.roles.join(', ')}</td>
                <td className="cell-actions"><button className="btn btn-ghost btn-sm" onClick={() => setSelected(u)}>관리</button></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {q.data && q.data.totalPages > 1 && (
        <div className="pagination">
          <button className="btn btn-ghost btn-sm" disabled={page <= 1} onClick={() => setPage(page - 1)}>이전</button>
          <span>{page} / {q.data.totalPages}</span>
          <button className="btn btn-ghost btn-sm" disabled={page >= q.data.totalPages} onClick={() => setPage(page + 1)}>다음</button>
        </div>
      )}
      {selected && <UserDialog user={selected} onClose={() => setSelected(null)} />}
    </section>
  );
}

function UserDialog({ user, onClose }: { user: UserRow; onClose: () => void }) {
  const toast = useToast();
  const queryClient = useQueryClient();
  const statuses = useQuery({ queryKey: ['admin', 'statuses', '회원'], queryFn: () => api<{ statusId: number; name: string }[]>('/api/statuses', { query: { statusTypeName: '회원' } }) });
  const grades = useQuery({ queryKey: ['admin', 'grades', 'all'], queryFn: () => api<{ gradeId: number; name: string }[]>('/api/grades') });
  const roles = useQuery({ queryKey: ['admin', 'userRoles', user.userId], queryFn: () => api<{ roleId: number; name: string }[]>(`/api/users/${user.userId}/roles`) });
  const unassigned = useQuery({ queryKey: ['admin', 'userRolesUn', user.userId], queryFn: () => api<{ roleId: number; name: string }[]>(`/api/users/${user.userId}/roles/unassigned`) });
  const [statusId, setStatusId] = useState('');
  const [gradeId, setGradeId] = useState('');

  const refresh = () => queryClient.invalidateQueries({ queryKey: ['admin'] });
  const run = async (fn: () => Promise<{ message: string | null }>) => {
    try {
      const res = await fn();
      toast(res.message ?? '처리했어요.');
      await refresh();
    } catch (e) {
      toast((e as Error).message, 'error');
    }
  };

  const currentStatus = statuses.data?.find((s) => s.name === user.statusName)?.statusId;
  const currentGrade = grades.data?.find((g) => g.name === user.gradeName)?.gradeId;

  return (
    <Modal title={`${user.nickname} (ID ${user.userId})`} onClose={onClose}>
      <div className="form-grid">
        <div className="field">
          <label>상태</label>
          <select className="select" value={statusId || String(currentStatus ?? '')} onChange={(e) => setStatusId(e.target.value)}>
            {statuses.data?.map((s) => <option key={s.statusId} value={s.statusId}>{s.name}</option>)}
          </select>
        </div>
        <div className="field">
          <label>등급</label>
          <select className="select" value={gradeId || String(currentGrade ?? '')} onChange={(e) => setGradeId(e.target.value)}>
            {grades.data?.map((g) => <option key={g.gradeId} value={g.gradeId}>{g.name}</option>)}
          </select>
        </div>
      </div>
      <button className="btn btn-primary" onClick={() => run(() => request<void>(`/api/users/${user.userId}/attribute`, {
        method: 'PUT', body: { statusId: Number(statusId || currentStatus), gradeId: Number(gradeId || currentGrade) },
      }))}>상태·등급 저장</button>
      <div className="field">
        <label>역할</label>
        <div className="chips" style={{ flexWrap: 'wrap' }}>
          {roles.data?.map((r) => (
            <button key={r.roleId} className="chip chip-sm active" title="눌러서 회수"
              onClick={() => confirm(`${r.name} 역할을 회수할까요?`) && run(() => request<void>(`/api/users/${user.userId}/roles/${r.roleId}`, { method: 'DELETE' }))}>
              {r.name} ✕
            </button>
          ))}
          {unassigned.data?.map((r) => (
            <button key={r.roleId} className="chip chip-sm" title="눌러서 부여"
              onClick={() => run(() => request<void>(`/api/users/${user.userId}/roles`, { method: 'POST', query: { roleId: r.roleId } }))}>
              + {r.name}
            </button>
          ))}
        </div>
      </div>
    </Modal>
  );
}

// ===== 게시물·댓글·검색어 =====
type PostRow = { postId: number; writer: string; title: string; viewCount: number; recommendationCount: number; createdAt: string };

function Posts() {
  const toast = useToast();
  const queryClient = useQueryClient();
  const [page, setPage] = useState(1);
  const q = useQuery({ queryKey: ['admin', 'posts', page], queryFn: () => api<Page<PostRow>>('/api/posts/all/paging', { query: { page, size: 30 } }), placeholderData: keepPreviousData });
  const run = async (fn: () => Promise<{ message: string | null }>) => {
    try {
      toast((await fn()).message ?? '처리했어요.');
      await queryClient.invalidateQueries({ queryKey: ['admin', 'posts'] });
    } catch (e) {
      toast((e as Error).message, 'error');
    }
  };
  return (
    <section className="stack" style={{ gap: 14 }}>
      <h2 className="section-title">게시물 {q.data && <span className="muted" style={{ fontSize: 14 }}>{formatNumber(q.data.totalElements)}개</span>}</h2>
      <div className="table-wrap">
        <table className="table">
          <thead><tr><th>ID</th><th>제목</th><th>작성자</th><th>조회</th><th>작성일</th><th /></tr></thead>
          <tbody>
            {q.data?.content.map((p) => (
              <tr key={p.postId}>
                <td>{p.postId}</td>
                <td className="wrap"><Link to={`/posts/${p.postId}`} target="_blank">{p.title}</Link></td>
                <td>{p.writer}</td><td>{formatNumber(p.viewCount)}</td><td>{formatDate(p.createdAt)}</td>
                <td className="cell-actions">
                  <button className="btn btn-ghost btn-sm" onClick={() => run(() => request<void>(`/api/posts/${p.postId}/isFixed`, { method: 'PUT', query: { isFixed: true } }))}>고정</button>
                  <button className="btn btn-ghost btn-sm" onClick={() => run(() => request<void>(`/api/posts/${p.postId}/isFixed`, { method: 'PUT', query: { isFixed: false } }))}>고정 해제</button>
                  <button className="btn btn-ghost btn-sm" onClick={() => confirm('이벤트를 종료 처리할까요?') && run(() => request<void>(`/api/posts/${p.postId}/finish`, { method: 'PUT' }))}>이벤트 종료</button>
                  <button className="btn btn-ghost btn-sm" style={{ color: 'var(--hot)' }} onClick={() => confirm('삭제할까요?') && run(() => request<void>(`/api/posts/${p.postId}`, { method: 'DELETE' }))}>삭제</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {q.data && (
        <div className="pagination">
          <button className="btn btn-ghost btn-sm" disabled={page <= 1} onClick={() => setPage(page - 1)}>이전</button>
          <span>{page} / {q.data.totalPages}</span>
          <button className="btn btn-ghost btn-sm" disabled={page >= q.data.totalPages} onClick={() => setPage(page + 1)}>다음</button>
        </div>
      )}
    </section>
  );
}

function Keywords() {
  const toast = useToast();
  const queryClient = useQueryClient();
  const q = useQuery({ queryKey: ['admin', 'keywords'], queryFn: () => api<string[]>('/api/searches/keywords') });
  return (
    <section className="stack" style={{ gap: 14 }}>
      <h2 className="section-title">인기 검색어</h2>
      <p className="section-desc" style={{ margin: 0 }}>부적절한 검색어는 눌러서 지울 수 있어요. 5분마다 순위를 다시 계산해요.</p>
      <div className="chips" style={{ flexWrap: 'wrap' }}>
        {q.data?.map((k, i) => (
          <button key={k} className="chip chip-sm" onClick={async () => {
            if (!confirm(`"${k}" 을(를) 지울까요?`)) return;
            const res = await request<void>('/api/searches/keywords', { method: 'DELETE', query: { keyword: k } }).catch((e) => toast((e as Error).message, 'error'));
            if (res) toast(res.message);
            await queryClient.invalidateQueries({ queryKey: ['admin', 'keywords'] });
          }}>
            {i + 1}. {k} ✕
          </button>
        ))}
        {q.data?.length === 0 && <span className="muted">아직 검색어가 없어요.</span>}
      </div>
    </section>
  );
}

// ===== 페이지 =====
const MENU: { group: string; items: { key: string; label: string }[] }[] = [
  { group: '현황', items: [{ key: 'dashboard', label: '대시보드' }] },
  {
    group: '운영',
    items: [
      { key: 'postReports', label: '게시물 신고' },
      { key: 'commentReports', label: '댓글 신고' },
      { key: 'posts', label: '게시물' },
      { key: 'users', label: '회원' },
      { key: 'userStops', label: '이용 정지' },
      { key: 'keywords', label: '인기 검색어' },
    ],
  },
  {
    group: '설정',
    items: [
      { key: 'categories', label: '카테고리' },
      { key: 'grades', label: '등급' },
      { key: 'points', label: '포인트 정책' },
      { key: 'banNicknames', label: '금지 닉네임' },
      { key: 'reportTypes', label: '신고 사유' },
      { key: 'roles', label: '역할' },
      { key: 'statuses', label: '상태' },
      { key: 'statusTypes', label: '상태 유형' },
    ],
  },
  { group: '시스템', items: [{ key: 'integrations', label: '외부 연동' }] },
];

export function AdminPage() {
  const { section = 'dashboard' } = useParams();
  const { isAdmin, loading } = useAuth();
  useDocumentTitle('관리자');
  if (loading) return null;
  if (!isAdmin) return <Navigate to="/" replace />;

  return (
    <div className="page">
      <div className="container">
        <h1 className="page-title" style={{ marginBottom: 16 }}>관리자</h1>
        <div className="admin-layout">
          <nav className="admin-nav" aria-label="관리 메뉴">
            {MENU.map((g) => (
              <div key={g.group} style={{ display: 'contents' }}>
                <div className="group hide-mobile">{g.group}</div>
                {g.items.map((item) => (
                  <Link key={item.key} to={`/admin/${item.key}`} className={section === item.key ? 'active' : ''}>
                    {item.label}
                  </Link>
                ))}
              </div>
            ))}
          </nav>
          <div style={{ minWidth: 0 }}>
            {section === 'dashboard' && <Dashboard />}
            {section === 'users' && <Users />}
            {section === 'posts' && <Posts />}
            {section === 'keywords' && <Keywords />}
            {section === 'integrations' && <IntegrationsAdmin />}
            {RESOURCES[section] && <ResourceManager key={section} config={RESOURCES[section]} />}
          </div>
        </div>
      </div>
    </div>
  );
}

export default AdminPage;
