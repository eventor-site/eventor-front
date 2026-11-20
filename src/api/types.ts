// 백엔드 응답 타입(eventor-back 의 DTO 와 이름을 맞춘다)

export type Page<T> = {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number; // 0부터
  size: number;
  first: boolean;
  last: boolean;
  empty: boolean;
};

export type EventStatus = '예정' | '진행중' | '마감' | '미정';

/** /api/posts/search 결과 한 줄 */
export type PostSummary = {
  postId: number;
  categoryName: string;
  statusName: string;
  writer: string;
  writerGrade: string | null;
  title: string;
  recommendationCount: number;
  viewCount: number;
  createdAt: string;
  productName: string | null;
  shoppingMall: string | null;
  eventStatusName: EventStatus | null;
  remainingDay: number | null;
  startTime: string | null;
  endTime: string | null;
  endType: string | null;
  imageUrl: string | null;
  imageType: string | null;
  /** 목록 화면용(2026-11-20) */
  commentCount?: number | null;
  isFixed?: boolean | null;
  price?: number | null;
  excerpt?: string | null;
  valueNote?: string | null;
  domain?: string | null;
};

/** 메인 화면 카드 */
export type MainPost = {
  postId: number;
  title: string;
  writer?: string;
  writerGrade?: string | null;
  recommendationCount?: number;
  viewCount?: number;
  createdAt?: string;
  startTime?: string | null;
  endTime?: string | null;
  imageUrl: string | null;
  imageType: string | null;
};

export type PostImage = {
  imageId: number;
  originalName: string;
  url: string;
  extension: string;
  type: 'image' | 'video' | string;
  size: number;
  isThumbnail: boolean;
  isPasted: boolean;
};

export type PostDetail = {
  postId: number;
  categoryName: string;
  statusName: string;
  writer: string;
  writerGrade: string | null;
  title: string;
  content: string;
  recommendationCount: number;
  viewCount: number;
  createdAt: string;
  isFixed: boolean;
  link: string | null;
  startTime: string | null;
  endTime: string | null;
  endType: string | null;
  shoppingMall: string | null;
  productName: string | null;
  price: number | null;
  images: PostImage[];
};

export type Comment = {
  commentId: number;
  parentCommentId: number | null;
  parentWriter: string | null;
  writer: string;
  writerGrade: string | null;
  content: string;
  recommendationCount: number;
  decommendationCount: number;
  createdAt: string;
  deletedAt: string | null;
  isAuthorized: boolean;
  depth: number;
};

export type CategoryNode = {
  categoryId: number;
  name: string;
  depth: number;
  children: CategoryNode[];
};

export type Session = { userId: number; roles: string[] } | null;

export type Bookmark = { bookmarkId: number; categoryName: string };


export type Me = {
  name: string | null;
  nickname: string;
  email: string | null;
  phone: string | null;
  birth: string | null;
  gender: string | null;
  point: number;
  statusName: string;
  gradeName: string;
  roles: string[];
  oauthType: string | null;
  createdAt: string;
  updatedAt: string | null;
  nicknameChangedAt: string | null;
  loginAt: string | null;
};

export type MyPost = {
  postId: number;
  writer: string;
  title: string;
  recommendationCount: number;
  viewCount: number;
  createdAt: string;
};

export type MyComment = {
  postId: number;
  commentId: number;
  content: string;
  recommendationCount: number;
  decommendationCount: number;
  createdAt: string;
};

export type MyFavorite = { favoriteId: number; postId: number; writer: string; title: string; createdAt: string };

export type MyPostView = { postId: number; title: string; imageUrl: string | null; imageType: string | null };

export type ReportType = { reportTypeId: number; name: string };

export type TempPost = {
  postId: number;
  categoryName: string;
  title: string;
  content: string;
  [key: string]: unknown;
} | null;

/** 글쓰기 요청(백엔드 CreatePostRequest·UpdatePostRequest) */
export type PostForm = {
  categoryName: string;
  title: string;
  content: string;
  link: string | null;
  startTime: string | null;
  endTime: string | null;
  endType: string | null;
  shoppingMall: string | null;
  productName: string | null;
  price: number | null;
};

export type FestivalItem = {
  contentId: string;
  contentTypeId: string;
  title: string;
  addr1: string | null;
  addr2: string | null;
  eventStartDate: string;
  eventEndDate: string;
  firstImage: string | null;
  tel: string | null;
  eventStatusName: '진행중' | '예정' | '마감' | string;
  remainingDay: number | null;
  rawEventStartDate: string;
};

export type TourDetail = {
  contentId: string;
  contentTypeId: string;
  title: string;
  overview: string | null;
  tel: string | null;
  homepage: string | null;
  address: string | null;
  zipcode: string | null;
  mapX: string | null;
  mapY: string | null;
  infoCenter: string | null;
  openDate: string | null;
  restDate: string | null;
  useTime: string | null;
  parking: string | null;
  admission: string | null;
  images: string[];
};

// ===== AI 인사이트 =====
export const DOMAINS = ['음식·카페', '패션·뷰티', '디지털·가전', '생활·리빙', '문화·여가', '금융·카드', '여행', '기타'] as const;
export const BENEFITS = ['할인·쿠폰', '1+1·증정', '응모·추첨', '체험단', '출석·적립', '신규가입', '무료'] as const;

export type MiniEvent = {
  postId: number;
  title: string;
  imageUrl?: string | null;
  endTime?: string | null;
  endType?: string | null;
  brand?: string | null;
  valueNote?: string | null;
};

export type PostInsight =
  | { analyzed: false; similar: MiniEvent[] }
  | {
      analyzed: true;
      domain: string | null;
      benefits: string[];
      brand: string | null;
      summary: string[];
      target: string | null;
      howTo: string | null;
      difficulty: '쉬움' | '보통' | '어려움' | null;
      valueNote: string | null;
      liveStatus: '진행 중' | '종료' | '불명' | null;
      liveReason: string | null;
      checkedAt: string | null;
      similar: MiniEvent[];
    };

export type InsightReport = {
  weekStart: string;
  headline: string;
  body: string;
  highlights: string | null; // JSON 문자열: {postId,title,reason}[]
  model: string | null;
  createdAt: string;
};

export type InsightsOverview = {
  ongoing: number;
  newThisWeek: number;
  endedByAi: number;
  endingSoon: MiniEvent[];
  byDomain: Record<string, number>;
  byBenefit: Record<string, number>;
  difficulty: { name: string; count: number }[];
  topBrands: { name: string; ongoing: number }[];
  easyPicks: MiniEvent[];
  monthlyNew: Record<string, number>;
  report: InsightReport | null;
};

export type BrandRow = { name: string; total: number; ongoing: number; lastPostedAt: string };

export type BrandDetail = {
  name: string;
  total: number;
  ongoing: MiniEvent[];
  past: { postId: number; title: string; startTime: string | null; endTime: string | null }[];
  stats: { firstAt: string | null; lastAt: string | null; avgDays: number | null; activeMonths: number };
  byBenefit: Record<string, number>;
};

export type LinkPreview = {
  title: string;
  brand: string;
  startDate: string | null;
  endDate: string | null;
  endType: string | null;
  domain: string | null;
  benefits: string[];
  summary: string[];
  link: string;
  remainingToday: number;
};

