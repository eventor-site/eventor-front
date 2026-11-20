import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter } from 'react-router';
import { App } from './App';
import { ErrorBoundary } from './components/ErrorBoundary';
import { AuthProvider } from './auth/AuthContext';
import { ToastProvider } from './components/Toast';
import { statsApi } from './api/endpoints';
import './styles/global.css';

// 방문자 수: 브라우저마다 하루 한 번(옛 사이트는 자정까지 유지되는 쿠키로 셌다)
try {
  const today = new Date().toLocaleDateString('ko-KR');
  if (localStorage.getItem('eventor.visitDate') !== today) {
    localStorage.setItem('eventor.visitDate', today);
    void statsApi.visit().catch(() => undefined);
  }
} catch {
  /* 저장소를 못 쓰는 환경 */
}

const queryClient = new QueryClient({
  defaultOptions: {
    queries: { refetchOnWindowFocus: false, retry: 1, staleTime: 30_000 },
  },
});

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <ToastProvider>
          <AuthProvider>
            <ErrorBoundary>
              <App />
            </ErrorBoundary>
          </AuthProvider>
        </ToastProvider>
      </BrowserRouter>
    </QueryClientProvider>
  </StrictMode>,
);
