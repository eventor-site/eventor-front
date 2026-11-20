import type { ReactNode } from 'react';
import { Icon } from './Icon';

export function Empty({ title, children }: { title: string; children?: ReactNode }) {
  return (
    <div className="empty">
      <Icon name="search" size={32} />
      <strong>{title}</strong>
      {children}
    </div>
  );
}

export function ErrorBox({ error, onRetry }: { error: unknown; onRetry?: () => void }) {
  const message = error instanceof Error ? error.message : '불러오지 못했습니다.';
  return (
    <div className="empty">
      <strong>문제가 생겼어요</strong>
      <span>{message}</span>
      {onRetry && (
        <button className="btn btn-outline btn-sm" onClick={onRetry}>
          다시 시도
        </button>
      )}
    </div>
  );
}

export function CardSkeletons({ count = 8 }: { count?: number }) {
  return (
    <>
      {Array.from({ length: count }, (_, i) => (
        <div key={i} className="card" aria-hidden="true">
          <div className="thumb skeleton" style={{ borderRadius: 0 }} />
          <div className="card-body">
            <div className="skeleton" style={{ height: 12, width: '40%' }} />
            <div className="skeleton" style={{ height: 16, width: '90%' }} />
            <div className="skeleton" style={{ height: 16, width: '70%' }} />
          </div>
        </div>
      ))}
    </>
  );
}

export function RowSkeletons({ count = 6 }: { count?: number }) {
  return (
    <>
      {Array.from({ length: count }, (_, i) => (
        <div key={i} className="row" aria-hidden="true">
          <div className="row-thumb skeleton" />
          <div className="row-body">
            <div className="skeleton" style={{ height: 16, width: '85%' }} />
            <div className="skeleton" style={{ height: 12, width: '40%' }} />
          </div>
        </div>
      ))}
    </>
  );
}
