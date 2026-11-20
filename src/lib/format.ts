// 날짜·숫자 표시. 서버 시간은 한국 시간(시간대 없는 ISO 문자열)이다.

function parse(value: string | null | undefined): Date | null {
  if (!value) return null;
  const d = new Date(value.length > 23 ? value.slice(0, 23) : value);
  return Number.isNaN(d.getTime()) ? null : d;
}

export function timeAgo(value: string | null | undefined): string {
  const d = parse(value);
  if (!d) return '';
  const diff = (Date.now() - d.getTime()) / 1000;
  if (diff < 60) return '방금 전';
  if (diff < 3600) return `${Math.floor(diff / 60)}분 전`;
  if (diff < 86400) return `${Math.floor(diff / 3600)}시간 전`;
  if (diff < 86400 * 7) return `${Math.floor(diff / 86400)}일 전`;
  return formatDate(value);
}

export function formatDate(value: string | null | undefined, withTime = false): string {
  const d = parse(value);
  if (!d) return '';
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  const base = `${y}.${m}.${day}`;
  if (!withTime) return base;
  return `${base} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`;
}

export function formatPeriod(start: string | null | undefined, end: string | null | undefined, endType?: string | null) {
  const s = formatDate(start);
  const e = end ? formatDate(end) : endType || '종료일 미정';
  if (!s && !end) return endType || '';
  return `${s || '시작일 미정'} ~ ${e}`;
}

/** 이벤트 남은 날짜 배지. 시작 전이면 'N일 후 시작' */
export function dday(start: string | null | undefined, end: string | null | undefined): string | null {
  const now = Date.now();
  const s = parse(start);
  const e = parse(end);
  if (s && s.getTime() > now) {
    const days = Math.ceil((s.getTime() - now) / 86400000);
    return days <= 1 ? '내일 시작' : `${days}일 후 시작`;
  }
  if (!e) return null;
  const left = e.getTime() - now;
  if (left < 0) return '마감';
  const days = Math.floor(left / 86400000);
  if (days === 0) return 'D-DAY';
  return `D-${days}`;
}

export function formatNumber(n: number | null | undefined): string {
  if (n === null || n === undefined) return '0';
  if (n >= 10000) return `${(n / 10000).toFixed(n >= 100000 ? 0 : 1)}만`;
  return n.toLocaleString('ko-KR');
}

export function formatPrice(n: number | null | undefined): string {
  if (n === null || n === undefined) return '';
  return `${n.toLocaleString('ko-KR')}원`;
}

/** "[쇼핑몰] 상품명 (가격) (배송)" 형식 제목에서 앞의 [쇼핑몰]을 떼어 낸다 */
export function splitBracketPrefix(title: string): { prefix: string | null; rest: string } {
  const m = /^\s*\[([^\]]{1,30})\]\s*(.*)$/.exec(title);
  return m ? { prefix: m[1], rest: m[2] } : { prefix: null, rest: title };
}
