import { useEffect, useId, useRef, useState, type KeyboardEvent, type ReactNode } from 'react';
import { createPortal } from 'react-dom';
import { Icon } from './Icon';
import '@/styles/dropdown.css';

/**
 * 기본 select 대신 쓰는 드롭다운(정렬·지역 같은 필터용).
 * - 데스크톱: 버튼 아래 팝오버, 모바일(640px 미만): 아래에서 올라오는 시트
 * - 키보드: ↑↓ 이동, Home/End, Enter·Space 선택, Esc 닫기 / 바깥을 누르면 닫힘
 * - ARIA: 버튼 aria-haspopup="listbox", 목록 role="listbox", 항목 role="option"
 */
export type DropdownOption<T extends string> = { value: T; label: string; hint?: string; icon?: ReactNode };

export function Dropdown<T extends string>({
  value,
  options,
  onChange,
  label,
  icon,
  align = 'end',
}: {
  value: T;
  options: DropdownOption<T>[];
  onChange: (value: T) => void;
  /** 화면 읽기·모바일 시트 제목 */
  label: string;
  icon?: ReactNode;
  align?: 'start' | 'end';
}) {
  const [open, setOpen] = useState(false);
  const [active, setActive] = useState(0);
  const [sheet, setSheet] = useState(false);
  const wrapRef = useRef<HTMLDivElement>(null);
  const listRef = useRef<HTMLUListElement>(null);
  const buttonRef = useRef<HTMLButtonElement>(null);
  const id = useId();
  const current = options.find((o) => o.value === value) ?? options[0];

  const show = () => {
    setActive(Math.max(0, options.findIndex((o) => o.value === value)));
    setSheet(window.matchMedia?.('(max-width: 639px)').matches ?? false);
    setOpen(true);
  };
  const close = (focusButton = true) => {
    setOpen(false);
    if (focusButton) buttonRef.current?.focus();
  };
  const choose = (i: number) => {
    const o = options[i];
    if (o) onChange(o.value);
    close();
  };

  // 열리면 목록에 포커스, 바깥을 누르면 닫기
  useEffect(() => {
    if (!open) return;
    listRef.current?.focus();
    const onDown = (e: MouseEvent) => {
      const t = e.target as Node;
      if (!wrapRef.current?.contains(t) && !listRef.current?.contains(t)) close(false);
    };
    document.addEventListener('mousedown', onDown);
    return () => document.removeEventListener('mousedown', onDown);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [open]);

  const onKey = (e: KeyboardEvent) => {
    const last = options.length - 1;
    const keys: Record<string, () => void> = {
      ArrowDown: () => setActive((i) => Math.min(last, i + 1)),
      ArrowUp: () => setActive((i) => Math.max(0, i - 1)),
      Home: () => setActive(0),
      End: () => setActive(last),
      Enter: () => choose(active),
      ' ': () => choose(active),
      Escape: () => close(),
      Tab: () => close(false),
    };
    const fn = keys[e.key];
    if (fn) {
      if (e.key !== 'Tab') e.preventDefault();
      fn();
    }
  };

  const list = (
    <ul
      ref={listRef}
      id={`${id}-list`}
      role="listbox"
      aria-label={label}
      tabIndex={-1}
      aria-activedescendant={`${id}-opt-${active}`}
      className={`dd-list ${sheet ? 'dd-sheet' : `dd-pop dd-${align}`}`}
      onKeyDown={onKey}
    >
      {sheet && <li className="dd-sheet-title" aria-hidden="true">{label}</li>}
      {options.map((o, i) => (
        <li
          key={o.value}
          id={`${id}-opt-${i}`}
          role="option"
          aria-selected={o.value === value}
          className={`dd-option ${i === active ? 'is-active' : ''} ${o.value === value ? 'is-selected' : ''}`}
          onMouseEnter={() => setActive(i)}
          onClick={() => choose(i)}
        >
          {o.icon && <span className="dd-option-icon">{o.icon}</span>}
          <span className="dd-option-text">
            <span>{o.label}</span>
            {o.hint && <small>{o.hint}</small>}
          </span>
          {o.value === value && <span className="dd-check" aria-hidden="true">✓</span>}
        </li>
      ))}
    </ul>
  );

  return (
    <div className="dd" ref={wrapRef}>
      <button
        ref={buttonRef}
        type="button"
        className={`dd-button ${open ? 'is-open' : ''}`}
        aria-haspopup="listbox"
        aria-expanded={open}
        aria-controls={open ? `${id}-list` : undefined}
        aria-label={`${label}: ${current?.label ?? ''}`}
        onClick={() => (open ? close() : show())}
        onKeyDown={(e) => {
          if (!open && (e.key === 'ArrowDown' || e.key === 'ArrowUp')) {
            e.preventDefault();
            show();
          }
        }}
      >
        {icon && <span className="dd-button-icon">{icon}</span>}
        <span className="dd-button-label">{current?.label}</span>
        <Icon name="chevron-right" size={16} />
      </button>
      {open &&
        (sheet
          ? createPortal(
              <div className="dd-backdrop" onMouseDown={() => close(false)}>
                <div onMouseDown={(e) => e.stopPropagation()}>{list}</div>
              </div>,
              document.body,
            )
          : list)}
    </div>
  );
}

/** 정렬 아이콘(위아래 화살표) */
export function SortIcon() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
      <path d="M7 4v16M3 8l4-4 4 4M17 20V4M13 16l4 4 4-4" />
    </svg>
  );
}
