// 비로그인 방문자 구분 값(게시물 조회 수 중복 방지에 쓴다). 브라우저마다 하나.
const KEY = 'eventor.viewerId';

export function getViewerId(): string {
  try {
    let id = localStorage.getItem(KEY);
    if (!id) {
      id = crypto.randomUUID();
      localStorage.setItem(KEY, id);
    }
    return id;
  } catch {
    return 'anonymous';
  }
}
