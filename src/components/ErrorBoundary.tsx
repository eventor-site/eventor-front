import { Component, type ReactNode } from 'react';

/** 화면 그리기 중 예상하지 못한 오류가 나도 흰 화면 대신 안내를 보여 준다 */
export class ErrorBoundary extends Component<{ children: ReactNode }, { error: Error | null }> {
  state = { error: null as Error | null };

  static getDerivedStateFromError(error: Error) {
    return { error };
  }

  componentDidCatch(error: Error) {
    console.error(error);
  }

  render() {
    if (!this.state.error) return this.props.children;
    return (
      <div className="page">
        <div className="container">
          <div className="empty">
            <strong>화면을 그리는 중 문제가 생겼어요</strong>
            <span>새로고침해도 계속되면 잠시 후 다시 이용해 주세요.</span>
            <div style={{ display: 'flex', gap: 8 }}>
              <button className="btn btn-primary" onClick={() => window.location.reload()}>새로고침</button>
              <a className="btn btn-outline" href="/">홈으로</a>
            </div>
          </div>
        </div>
      </div>
    );
  }
}
