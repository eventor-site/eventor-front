import type { QueryClient } from '@tanstack/react-query';

/**
 * 글을 쓰거나 고치거나 지운 뒤 화면 캐시를 정리한다(2026-11-20).
 * 목록 캐시는 30초 동안 "최신"으로 취급돼, 정리하지 않으면 지운 글이 새로고침 전까지 목록에 남았다.
 * - 지운 글: 상세·AI 분석 캐시를 없앤다
 * - 목록·메인·트렌드·브랜드·마이페이지: 지금 화면에 있는 것은 바로 다시 받고, 나머지는 다음에 열 때 다시 받는다
 */
export async function refreshAfterPostChange(queryClient: QueryClient, postId?: number, removed = false) {
  if (postId && removed) {
    queryClient.removeQueries({ queryKey: ['post', postId] });
    queryClient.removeQueries({ queryKey: ['insight', postId] });
  } else if (postId) {
    await queryClient.invalidateQueries({ queryKey: ['post', postId] });
  }
  await Promise.all(
    ['posts', 'main', 'home', 'insights', 'brand', 'me', 'admin'].map((key) =>
      queryClient.invalidateQueries({ queryKey: [key] }),
    ),
  );
}
