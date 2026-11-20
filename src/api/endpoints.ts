import { api, request } from './client';
import type { BrandDetail, BrandRow, InsightsOverview, LinkPreview, PostInsight } from './types';
import type {
  Bookmark,
  CategoryNode,
  Comment,
  MainPost,
  Page,
  PostDetail,
  PostSummary,
  Session,
} from './types';

export type PostSearchParams = {
  page?: number; // 1부터
  size?: number;
  keyword?: string;
  categoryName?: string;
  eventStatusName?: string;
  endType?: string;
  sort?: string; // 예: createdAt,desc
  /** AI 분석 기준 분야(음식·카페 …) */
  domain?: string;
  /** AI 분석 기준 혜택 유형(할인·쿠폰 …) */
  benefit?: string;
};

export const postsApi = {
  search: (p: PostSearchParams, signal?: AbortSignal) =>
    api<Page<PostSummary>>('/api/posts/search', { query: { page: 1, size: 20, ...p }, signal }),
  hotEvents: () => api<MainPost[]>('/api/posts/event/hot'),
  latestEvents: () => api<MainPost[]>('/api/posts/event/latest'),
  deadlineEvents: () => api<MainPost[]>('/api/posts/event/deadline'),
  recommendedEvents: () => api<MainPost[]>('/api/posts/event/recommendation'),
  trendingEvents: () => api<MainPost[]>('/api/posts/event/trending'),
  hotDeals: () => api<MainPost[]>('/api/posts/hotDeal'),
  community: () => api<MainPost[]>('/api/posts/community'),
  detail: (postId: number, viewerId: string) =>
    api<PostDetail>(`/api/posts/${postId}`, { query: { uuid: viewerId } }),
  canEdit: (postId: number) => api<boolean>(`/api/posts/${postId}/isAuthorized`),
  recommend: (postId: number) => request<void>(`/api/posts/${postId}/recommend`, { method: 'PUT' }),
  disrecommend: (postId: number) => request<void>(`/api/posts/${postId}/disrecommend`, { method: 'PUT' }),
  toggleFavorite: (postId: number) => request<void>(`/api/posts/${postId}/favorites`, { method: 'POST' }),
  remove: (postId: number) => request<void>(`/api/posts/${postId}`, { method: 'DELETE' }),
};

export const commentsApi = {
  list: (postId: number, page: number, size = 50) =>
    api<Page<Comment>>(`/api/posts/${postId}/comments/paging`, { query: { page, size } }),
  create: (postId: number, content: string, parentCommentId: number | null) =>
    request<void>(`/api/posts/${postId}/comments`, { method: 'POST', body: { content, parentCommentId } }),
  update: (commentId: number, content: string) =>
    request<void>(`/api/comments/${commentId}`, { method: 'PUT', body: { content } }),
  remove: (commentId: number) => request<void>(`/api/comments/${commentId}`, { method: 'DELETE' }),
  recommend: (commentId: number) => request<void>(`/api/comments/${commentId}/recommend`, { method: 'PUT' }),
  disrecommend: (commentId: number) =>
    request<void>(`/api/comments/${commentId}/disrecommend`, { method: 'PUT' }),
};

export const categoriesApi = {
  tree: () => api<CategoryNode[]>('/api/categories/tree'),
};

export const searchApi = {
  topKeywords: (limit = 10) => api<string[]>('/api/searches/keywords/top', { query: { limit } }),
};

export const tourApi = {
  ongoingFestivals: () => api<import('./types').FestivalItem[]>('/api/tours/festivals/ongoing'),
  festivals: () => api<import('./types').FestivalItem[]>('/api/tours/festival2'),
  detail: (contentId: string, contentTypeId: string) =>
    api<import('./types').TourDetail>(`/api/tours/${contentId}`, { query: { contentTypeId } }),
};

export const bookmarksApi = {
  mine: () => api<Bookmark[]>('/api/users/me/bookmarks'),
  add: (categoryName: string) =>
    request<void>(`/api/categories/${encodeURIComponent(categoryName)}/bookmarks`, { method: 'POST' }),
  remove: (bookmarkId: number) => request<void>(`/api/bookmarks/${bookmarkId}`, { method: 'DELETE' }),
};

export type LoginResult = { result: 'LOGGED_IN' | 'DORMANT' };

export const authApi = {
  session: () => api<Session>('/api/auth/session'),
  login: (identifier: string, password: string) =>
    api<LoginResult>('/api/auth/login', { method: 'POST', body: { identifier, password } }),
  logout: () => request<void>('/api/auth/logout', { method: 'POST' }),
  activateDormant: () => request<void>('/api/auth/dormant/activate', { method: 'POST' }),
  googlePending: () => api<{ email: string; name: string }>('/api/auth/google/pending'),
  googleSignup: (nickname: string) =>
    request<void>('/api/auth/google/signup', { method: 'POST', body: { nickname } }),
  googleLoginUrl: (redirect: string) => `/api/auth/google?redirect=${encodeURIComponent(redirect)}`,
};

// ===== 글쓰기 =====
export const writeApi = {
  temp: () => api<import('./types').TempPost>('/api/posts/temp'),
  deleteTemp: async () => {
    await request<void>('/api/images/temp', { method: 'DELETE' }).catch(() => undefined);
    return request<void>('/api/posts/temp', { method: 'DELETE' });
  },
  create: (form: import('./types').PostForm, isTemp: boolean) =>
    request<{ postId: number }>('/api/posts', { method: 'POST', body: form, query: { isTemp } }),
  update: (postId: number, form: import('./types').PostForm, isTemp: boolean) =>
    request<void>(`/api/posts/${postId}`, { method: 'PUT', body: form, query: { isTemp } }),
  uploadImage: (postId: number, categoryName: string, file: File, opts: { thumbnail?: boolean; pasted?: boolean } = {}) => {
    const form = new FormData();
    form.append('file', file);
    return api<import('./types').PostImage[]>('/api/images/upload', {
      method: 'POST',
      form,
      query: {
        folderName: 'postimage',
        postId,
        categoryName,
        isThumbnail: !!opts.thumbnail,
        isPasted: !!opts.pasted,
      },
    });
  },
  deleteImages: (postId: number, categoryName: string, imageIds: number[]) =>
    api<import('./types').PostImage[]>('/api/images', { method: 'DELETE', body: { postId, categoryName, imageIds } }),
};

// ===== 내 정보·마이페이지 =====
export const meApi = {
  info: () => api<import('./types').Me>('/api/users/me'),
  update: (body: { name: string | null; nickname: string; email: string | null; phone: string | null; birth: string | null; gender: string | null }) =>
    request<void>('/api/users/me', { method: 'PUT', body }),
  changePassword: (currentPassword: string, password: string) =>
    request<void>('/api/users/me/password', { method: 'PUT', body: { currentPassword, password } }),
  withdraw: () => request<void>('/api/users/me', { method: 'DELETE' }),
  checkNickname: (nickname: string) => request<void>('/api/users/me/checkNickname', { method: 'POST', body: { nickname } }),
  posts: (page: number) => api<Page<import('./types').MyPost>>('/api/posts/me/paging', { query: { page, size: 10 } }),
  comments: (page: number) =>
    api<Page<import('./types').MyComment>>('/api/users/me/comments/paging', { query: { page, size: 10 } }),
  favorites: (page: number) =>
    api<Page<import('./types').MyFavorite>>('/api/users/me/favorites/paging', { query: { page, size: 10 } }),
  removeFavorite: (favoriteId: number) => request<void>(`/api/favorites/${favoriteId}`, { method: 'DELETE' }),
  postViews: (page: number) =>
    api<Page<import('./types').MyPostView>>('/api/users/me/postViews/paging', { query: { page, size: 20 } }),
};

// ===== 회원가입·계정 찾기 =====
export const signupApi = {
  checkIdentifier: (identifier: string) =>
    request<void>('/api/users/signup/checkIdentifier', { method: 'POST', body: { identifier } }),
  checkNickname: (nickname: string) => request<void>('/api/users/signup/checkNickname', { method: 'POST', body: { nickname } }),
  sendCode: (email: string) =>
    request<boolean>('/api/users/signup/sendEmail', { method: 'POST', body: { email, type: '회원가입' } }),
  certify: (email: string, certifyCode: string) =>
    request<boolean>('/api/users/signup/certifyEmail', { method: 'POST', body: { email, type: '회원가입', certifyCode } }),
  signup: (body: { identifier: string; password: string; nickname: string; email: string }) =>
    request<void>('/api/users/signup', { method: 'POST', body }),
  findIdentifier: (email: string) => request<void>('/api/auth/identifier/find', { method: 'POST', body: { email } }),
  sendResetCode: (identifier: string) => request<void>('/api/auth/password/code', { method: 'POST', body: { identifier } }),
  resetPassword: (identifier: string, code: string, newPassword: string) =>
    request<void>('/api/auth/password/reset', { method: 'POST', body: { identifier, code, newPassword } }),
};

// ===== 신고 =====
export const reportApi = {
  types: () => api<import('./types').ReportType[]>('/api/reportTypes'),
  post: (postId: number, reportType: string) =>
    request<void>(`/api/posts/${postId}/postReports`, { method: 'POST', query: { reportTypeName: reportType } }),
  comment: (commentId: number, reportType: string) =>
    request<void>(`/api/comments/${commentId}/commentReports`, { method: 'POST', query: { reportTypeName: reportType } }),
};

// ===== 방문 통계(하루 한 번) =====
export const statsApi = {
  visit: () => request<void>('/api/statistics/visitors', { method: 'POST' }),
};

// ===== AI 인사이트(Gemini 분석 결과. 화면은 저장된 결과만 읽는다) =====

export const insightsApi = {
  post: (postId: number) => api<PostInsight>(`/api/posts/${postId}/insight`),
  overview: () => api<InsightsOverview>('/api/insights/overview'),
  brands: (limit = 40) => api<BrandRow[]>('/api/brands', { query: { limit } }),
  brand: (name: string) => api<BrandDetail>(`/api/brands/${encodeURIComponent(name)}`),
  linkPreview: (url: string) => api<LinkPreview>('/api/ai/link-preview', { method: 'POST', body: { url } }),
};

