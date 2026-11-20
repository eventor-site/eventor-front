import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { fileURLToPath, URL } from 'node:url';

// 로컬 개발: 브라우저는 http://localhost:9080 하나로 접속한다.
// /api(백엔드 API), /uploads(이미지), SEO 페이지·sitemap 은 백엔드(9081)로 넘긴다. 운영에서는 nginx 가 같은 일을 한다.
// Host 헤더를 바꾸지 않아야(changeOrigin: false) 백엔드가 구글 콜백 주소를 http://localhost:9080/... 로 만든다.
const BACKEND = 'http://localhost:9081';

export default defineConfig({
  plugins: [react()],
  resolve: { alias: { '@': fileURLToPath(new URL('./src', import.meta.url)) } },
  server: {
    port: 9080,
    strictPort: true,
    host: true,
    proxy: {
      '/api': { target: BACKEND, changeOrigin: false },
      '/uploads': { target: BACKEND, changeOrigin: false },
      // 검색엔진용 HTML(게시물 상세·카테고리 목록)과 사이트맵: 운영 nginx 와 같게 백엔드가 응답한다.
      // 화면 안에서 이동할 때는 React 가 처리하므로, 주소창에서 직접 열거나 새로고침할 때만 여기로 온다.
      '^/posts(/\\d+)?/?(\\?.*)?$': { target: BACKEND, changeOrigin: false },
      '^/sitemap\\.xml$': { target: BACKEND, changeOrigin: false },
      '^/sitemaps/': { target: BACKEND, changeOrigin: false },
    },
  },
  build: {
    sourcemap: false,
    chunkSizeWarningLimit: 800,
  },
});
