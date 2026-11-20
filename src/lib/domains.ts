import { DOMAINS } from '@/api/types';

/**
 * 사용자에게 보이는 분류(2026-11-20 개편): 게시판 4개 + 이벤트 분야 8개(AI 자동 분류).
 * DB 의 옛 카테고리 트리(이벤트 > 음식 > 피자 …)는 지우지 않고(옛 글·즐겨찾기·주소 보존) 화면에서만 쓰지 않는다.
 */
export type Domain = (typeof DOMAINS)[number];

export const DOMAIN_META: Record<Domain, { emoji: string; desc: string }> = {
  '음식·카페': { emoji: '🍕', desc: '외식·카페·배달' },
  '패션·뷰티': { emoji: '💄', desc: '옷·신발·화장품' },
  '디지털·가전': { emoji: '📱', desc: '전자기기·가전' },
  '생활·리빙': { emoji: '🏠', desc: '생활용품·가구' },
  '문화·여가': { emoji: '🎤', desc: '공연·영화·팝업·스포츠' },
  '금융·카드': { emoji: '💳', desc: '카드·예적금·포인트' },
  여행: { emoji: '✈️', desc: '숙박·항공·여행' },
  기타: { emoji: '🎁', desc: '그 밖의 혜택' },
};

/** 글을 쓸 수 있는 게시판(관리자). 회원은 자유·핫딜만 */
export const BOARDS = ['이벤트', '핫딜', '자유', '공지'] as const;

/**
 * 옛 이벤트 카테고리 → 분야. 2026-11-20 개발 DB 에서 카테고리마다 AI 분류가 어느 분야로 갔는지 센 결과,
 * 거의 모두 한 분야로 모였다(피자·치킨·카페 100% 음식·카페, 스킨케어 100% 패션·뷰티, 예·적금 100% 금융·카드 …).
 * 옛 주소(/posts?categoryName=피자)는 이 표로 /posts?categoryName=이벤트&domain=음식·카페 로 넘긴다.
 */
export const LEGACY_CATEGORY_TO_DOMAIN: Record<string, Domain> = {
  음식: '음식·카페',
  밥: '음식·카페',
  치킨: '음식·카페',
  피자: '음식·카페',
  패스트푸드: '음식·카페',
  '카페 & 디저트': '음식·카페',
  패션: '패션·뷰티',
  의류: '패션·뷰티',
  신발: '패션·뷰티',
  액세서리: '패션·뷰티',
  시즌기획전: '패션·뷰티',
  뷰티: '패션·뷰티',
  스킨케어: '패션·뷰티',
  메이크업: '패션·뷰티',
  향수: '패션·뷰티',
  전자제품: '디지털·가전',
  가전제품: '디지털·가전',
  '모바일 & PC': '디지털·가전',
  '건강/미용 가전': '디지털·가전',
  엔터: '문화·여가',
  '콘서트/공연': '문화·여가',
  '굿즈 & 팝업스토어': '문화·여가',
  스포츠: '문화·여가',
  영화: '문화·여가',
  금융: '금융·카드',
  카드혜택: '금융·카드',
  '예·적금': '금융·카드',
  '라이프 스타일': '생활·리빙',
  가구: '생활·리빙',
  생활용품: '생활·리빙',
};

/** 분야 목록 주소. ongoing 이면 진행 중만 */
export function domainHref(domain: string, ongoing = true) {
  const q = new URLSearchParams({ categoryName: '이벤트', domain });
  if (ongoing) q.set('eventStatusName', '진행중');
  return `/posts?${q.toString()}`;
}

export function benefitHref(benefit: string) {
  return `/posts?${new URLSearchParams({ categoryName: '이벤트', benefit, eventStatusName: '진행중' }).toString()}`;
}
