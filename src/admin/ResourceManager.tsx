import { keepPreviousData, useQuery, useQueryClient } from '@tanstack/react-query';
import { useState, type FormEvent, type ReactNode } from 'react';
import { api, request } from '@/api/client';
import type { Page } from '@/api/types';
import { Empty, ErrorBox } from '@/components/Feedback';
import { Modal } from '@/components/Modal';
import { useToast } from '@/components/Toast';

/**
 * 관리자 공통 목록·등록·수정·삭제 화면.
 * 대부분의 관리 데이터(등급, 포인트, 역할, 상태, 신고 유형, 금지 닉네임 …)가 같은 형태의 API 라서 설정만으로 화면을 만든다.
 *   GET {path}/paging?page=&size=  목록
 *   POST {path}                   등록
 *   PUT {path}/{id}               수정
 *   DELETE {path}/{id}            삭제
 */
export type Field = {
  name: string;
  label: string;
  type?: 'text' | 'number' | 'select';
  options?: { value: string | number; label: string }[];
  required?: boolean;
  help?: string;
};

export type Column<T> = { key: string; label: string; render?: (row: T) => ReactNode; width?: number | string };

export type ResourceConfig<T> = {
  title: string;
  path: string;
  idKey: keyof T & string;
  columns: Column<T>[];
  fields?: Field[];
  canCreate?: boolean;
  canUpdate?: boolean;
  canDelete?: boolean;
  description?: string;
  /** 행마다 추가 버튼 */
  actions?: (row: T, reload: () => void) => ReactNode;
  pageSize?: number;
};

export function ResourceManager<T extends Record<string, unknown>>({ config }: { config: ResourceConfig<T> }) {
  const toast = useToast();
  const queryClient = useQueryClient();
  const [page, setPage] = useState(1);
  const [editing, setEditing] = useState<T | 'new' | null>(null);
  const size = config.pageSize ?? 20;
  const queryKey = ['admin', config.path, page];
  const q = useQuery({
    queryKey,
    queryFn: () => api<Page<T>>(`${config.path}/paging`, { query: { page, size } }),
    placeholderData: keepPreviousData,
  });
  const reload = () => void queryClient.invalidateQueries({ queryKey: ['admin', config.path] });

  const remove = async (row: T) => {
    if (!confirm('삭제할까요? 되돌릴 수 없어요.')) return;
    try {
      const res = await request<void>(`${config.path}/${row[config.idKey]}`, { method: 'DELETE' });
      toast(res.message ?? '삭제했어요.');
      reload();
    } catch (e) {
      toast((e as Error).message, 'error');
    }
  };

  const data = q.data;
  return (
    <section className="stack" style={{ gap: 14 }}>
      <div className="row-between">
        <div>
          <h2 className="section-title">{config.title}</h2>
          {config.description && <p className="section-desc">{config.description}</p>}
        </div>
        {config.canCreate && config.fields && (
          <button className="btn btn-primary btn-sm" onClick={() => setEditing('new')}>
            + 추가
          </button>
        )}
      </div>
      {q.isError ? (
        <ErrorBox error={q.error} onRetry={() => q.refetch()} />
      ) : !data ? (
        <div className="skeleton" style={{ height: 200 }} />
      ) : data.content.length === 0 ? (
        <Empty title="데이터가 없어요" />
      ) : (
        <div className="table-wrap">
          <table className="table">
            <thead>
              <tr>
                {config.columns.map((c) => (
                  <th key={c.key} style={{ width: c.width }}>
                    {c.label}
                  </th>
                ))}
                {(config.canUpdate || config.canDelete || config.actions) && <th style={{ width: 1 }} />}
              </tr>
            </thead>
            <tbody>
              {data.content.map((row) => (
                <tr key={String(row[config.idKey])}>
                  {config.columns.map((c) => (
                    <td key={c.key}>{c.render ? c.render(row) : String(row[c.key] ?? '')}</td>
                  ))}
                  {(config.canUpdate || config.canDelete || config.actions) && (
                    <td className="cell-actions">
                      {config.actions?.(row, reload)}
                      {config.canUpdate && config.fields && (
                        <button className="btn btn-ghost btn-sm" onClick={() => setEditing(row)}>
                          수정
                        </button>
                      )}
                      {config.canDelete && (
                        <button className="btn btn-ghost btn-sm" style={{ color: 'var(--hot)' }} onClick={() => remove(row)}>
                          삭제
                        </button>
                      )}
                    </td>
                  )}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
      {data && data.totalPages > 1 && (
        <div className="pagination">
          <button className="btn btn-ghost btn-sm" disabled={page <= 1} onClick={() => setPage(page - 1)}>
            이전
          </button>
          <span>
            {page} / {data.totalPages}
          </span>
          <button className="btn btn-ghost btn-sm" disabled={page >= data.totalPages} onClick={() => setPage(page + 1)}>
            다음
          </button>
        </div>
      )}
      {editing && config.fields && (
        <EditDialog
          config={config}
          row={editing === 'new' ? null : editing}
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

function EditDialog<T extends Record<string, unknown>>({ config, row, onClose, onSaved }: {
  config: ResourceConfig<T>;
  row: T | null;
  onClose: () => void;
  onSaved: () => void;
}) {
  const toast = useToast();
  const [values, setValues] = useState<Record<string, string>>(() =>
    Object.fromEntries((config.fields ?? []).map((f) => [f.name, row?.[f.name] == null ? '' : String(row[f.name])])),
  );
  const [busy, setBusy] = useState(false);

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    setBusy(true);
    const body = Object.fromEntries(
      (config.fields ?? []).map((f) => [f.name, values[f.name] === '' ? null : f.type === 'number' ? Number(values[f.name]) : values[f.name]]),
    );
    try {
      const res = row
        ? await request<void>(`${config.path}/${row[config.idKey]}`, { method: 'PUT', body })
        : await request<void>(config.path, { method: 'POST', body });
      toast(res.message ?? '저장했어요.');
      onSaved();
    } catch (err) {
      toast((err as Error).message, 'error');
    } finally {
      setBusy(false);
    }
  };

  return (
    <Modal title={`${config.title} ${row ? '수정' : '추가'}`} onClose={onClose}>
      <form className="stack" onSubmit={submit}>
        {config.fields!.map((f) => (
          <div className="field" key={f.name}>
            <label htmlFor={`f-${f.name}`}>{f.label}</label>
            {f.type === 'select' ? (
              <select id={`f-${f.name}`} className="select" value={values[f.name]} required={f.required}
                onChange={(e) => setValues({ ...values, [f.name]: e.target.value })}>
                <option value="">선택</option>
                {f.options?.map((o) => (
                  <option key={o.value} value={o.value}>
                    {o.label}
                  </option>
                ))}
              </select>
            ) : (
              <input id={`f-${f.name}`} className="input" type={f.type === 'number' ? 'number' : 'text'} value={values[f.name]}
                required={f.required} onChange={(e) => setValues({ ...values, [f.name]: e.target.value })} />
            )}
            {f.help && <span className="help">{f.help}</span>}
          </div>
        ))}
        <button className="btn btn-primary btn-block" disabled={busy}>
          저장
        </button>
      </form>
    </Modal>
  );
}
