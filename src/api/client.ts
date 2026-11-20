// 백엔드 API 호출. 같은 도메인의 /api 를 부르고, 인증은 HttpOnly 쿠키가 자동으로 실린다.
// 응답 형식: { status: 'success' | 오류코드, data, message }

export type ApiEnvelope<T> = {
  status: string;
  data: T;
  message: string | null;
};

export class ApiError extends Error {
  readonly httpStatus: number;
  readonly data: unknown;

  constructor(message: string, httpStatus: number, data?: unknown) {
    super(message);
    this.httpStatus = httpStatus;
    this.data = data;
  }
}

type Query = Record<string, string | number | boolean | null | undefined>;

type RequestOptions = {
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE';
  query?: Query;
  body?: unknown;
  form?: FormData;
  signal?: AbortSignal;
};

export function buildUrl(path: string, query?: Query): string {
  const params = new URLSearchParams();
  if (query) {
    for (const [key, value] of Object.entries(query)) {
      if (value !== undefined && value !== null && value !== '') params.append(key, String(value));
    }
  }
  const qs = params.toString();
  return qs ? `${path}?${qs}` : path;
}

/** 응답 전체(data + message)를 돌려준다 */
export async function request<T>(path: string, options: RequestOptions = {}): Promise<ApiEnvelope<T>> {
  const { method = 'GET', query, body, form, signal } = options;
  const headers: Record<string, string> = { Accept: 'application/json' };
  let payload: BodyInit | undefined;
  if (form) {
    payload = form;
  } else if (body !== undefined) {
    headers['Content-Type'] = 'application/json';
    payload = JSON.stringify(body);
  }

  let response: Response;
  try {
    response = await fetch(buildUrl(path, query), {
      method,
      headers,
      body: payload,
      credentials: 'same-origin',
      signal,
    });
  } catch (e) {
    if ((e as Error).name === 'AbortError') throw e;
    throw new ApiError('네트워크 연결을 확인해 주세요.', 0);
  }

  let json: ApiEnvelope<T> | null = null;
  const text = await response.text();
  if (text) {
    try {
      json = JSON.parse(text) as ApiEnvelope<T>;
    } catch {
      json = null;
    }
  }

  if (!response.ok) {
    const message = json?.message || defaultMessage(response.status);
    throw new ApiError(message, response.status, json?.data);
  }
  return json ?? { status: 'success', data: undefined as T, message: null };
}

/** data 만 돌려준다 */
export async function api<T>(path: string, options?: RequestOptions): Promise<T> {
  return (await request<T>(path, options)).data;
}

function defaultMessage(status: number): string {
  if (status === 401) return '로그인이 필요합니다.';
  if (status === 403) return '권한이 없습니다.';
  if (status === 404) return '찾을 수 없습니다.';
  if (status === 413) return '파일 용량이 너무 큽니다.';
  if (status >= 500) return '서버 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.';
  return '요청을 처리하지 못했습니다.';
}
