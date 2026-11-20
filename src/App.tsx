import { lazy, Suspense } from 'react';
import { Navigate, Route, Routes } from 'react-router';
import { Layout } from '@/components/Layout';
import { DormantPage, GoogleSignupPage, LoginPage } from '@/pages/AuthPages';
import { HomePage } from '@/pages/HomePage';
import { NotFoundPage } from '@/pages/MiscPages';
import { MyPage } from '@/pages/MyPage';
import { FindAccountPage, SignupPage } from '@/pages/SignupPages';
import { WritePage } from '@/pages/WritePage';
import { FestivalPage, TourDetailPage } from '@/pages/TourPages';
import { PostDetailPage } from '@/pages/PostDetailPage';
import { PostListPage } from '@/pages/PostListPage';
import { PolicyPage } from '@/pages/PolicyPage';
import { InsightsPage } from '@/pages/InsightsPage';
import { BrandPage } from '@/pages/BrandPage';

// 관리자 화면은 일반 방문자에게 필요 없으니 따로 불러온다
const AdminPage = lazy(() => import('@/admin/AdminPage'));

export function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route index element={<HomePage />} />
        <Route path="posts" element={<PostListPage mode="category" />} />
        <Route path="posts/:postId" element={<PostDetailPage />} />
        <Route path="search" element={<PostListPage mode="search" />} />
        <Route path="insights" element={<InsightsPage />} />
        <Route path="brands/:name" element={<BrandPage />} />
        <Route path="auth/login" element={<LoginPage />} />
        <Route path="auth/dormant" element={<DormantPage />} />
        <Route path="auth/google/signup" element={<GoogleSignupPage />} />
        <Route path="auth/signup" element={<SignupPage />} />
        <Route path="auth/find" element={<FindAccountPage />} />
        <Route path="me" element={<MyPage />} />
        <Route path="me/:tab" element={<MyPage />} />
        <Route path="admin" element={<Suspense fallback={null}><AdminPage /></Suspense>} />
        <Route path="admin/:section" element={<Suspense fallback={null}><AdminPage /></Suspense>} />
        <Route path="tours" element={<Navigate to="/tours/festival2" replace />} />
        <Route path="tours/festival2" element={<FestivalPage />} />
        <Route path="tours/:contentId" element={<TourDetailPage />} />
        <Route path="write" element={<WritePage />} />
        <Route path="write/:postId" element={<WritePage />} />
        {/* 옛 사이트와 같은 주소(검색엔진·앱에 남아 있는 링크) */}
        <Route path="terms/1" element={<PolicyPage doc="privacy" />} />
        <Route path="terms/2" element={<PolicyPage doc="terms" />} />
        <Route path="privacy" element={<Navigate to="/terms/1" replace />} />
        <Route path="terms" element={<Navigate to="/terms/2" replace />} />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </Routes>
  );
}
