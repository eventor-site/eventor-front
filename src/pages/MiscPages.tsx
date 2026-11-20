import { Link } from 'react-router';
import { useDocumentTitle } from '@/lib/useDocumentTitle';

export function NotFoundPage() {
  useDocumentTitle('페이지를 찾을 수 없어요');
  return (
    <div className="page">
      <div className="container">
        <div className="empty">
          <strong>페이지를 찾을 수 없어요</strong>
          <span>주소가 바뀌었거나 삭제된 페이지예요.</span>
          <Link to="/" className="btn btn-primary">
            홈으로
          </Link>
        </div>
      </div>
    </div>
  );
}
