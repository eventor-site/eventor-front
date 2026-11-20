# eventor-front

이벤터(https://eventor.duckdns.org) 웹 프론트. React + TypeScript + Vite.
프로젝트 문서(기술 스택·설계·작업 기록)는 백엔드 저장소 `eventor-back/docs/` 에 있다.

## 로컬 실행

```bash
npm install
npm run dev        # http://localhost:9080
```

- 백엔드(`eventor-back`, 9081)를 먼저 띄운다. `/api`, `/uploads`, 게시물·카테고리 SEO 페이지, 사이트맵은 Vite 가 백엔드로 넘긴다(`vite.config.ts`).
- 구글 로그인 콜백: `http://localhost:9080/api/auth/google/callback`

## 빌드·배포

- `npm run build` → `dist/`
- `main` 에 push 하면 GitHub Actions 가 빌드해 서버 `/home/ubuntu/content/eventor/releases` 에 올리고 nginx 를 다시 읽힌다.

## 구조

| 폴더 | 내용 |
|---|---|
| `src/api` | 백엔드 호출(`client.ts`), 응답 타입, 엔드포인트 모음 |
| `src/auth` | 로그인 상태(쿠키 기반, `/api/auth/session`) |
| `src/components` | 공통 컴포넌트(레이아웃, 카드, 모달, 에디터, 토스트 …) |
| `src/pages` | 화면 |
| `src/admin` | 관리자 화면(설정 기반 공통 CRUD `ResourceManager`) |
| `src/content` | 개인정보처리방침·이용약관 원문 |
| `src/styles/global.css` | 디자인 토큰과 공통 스타일(라이트/다크) |
