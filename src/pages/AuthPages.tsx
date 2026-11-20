import { useQuery } from '@tanstack/react-query';
import { useState, type FormEvent } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { authApi } from '@/api/endpoints';
import { useAuth } from '@/auth/AuthContext';
import { Icon } from '@/components/Icon';
import { useToast } from '@/components/Toast';
import { useDocumentTitle } from '@/lib/useDocumentTitle';

const ERRORS: Record<string, string> = {
  google: '구글 로그인을 완료하지 못했어요. 다시 시도해 주세요.',
  withdrawn: '탈퇴한 계정이에요. 관리자에게 문의해 주세요.',
};

/** 로그인 후 돌아갈 주소(같은 사이트 경로만) */
function safeRedirect(value: string | null): string {
  if (!value || !value.startsWith('/') || value.startsWith('//') || value.startsWith('/auth')) return '/';
  return value;
}

export function LoginPage() {
  useDocumentTitle('로그인');
  const [params] = useSearchParams();
  const navigate = useNavigate();
  const { refresh } = useAuth();
  const redirect = safeRedirect(params.get('redirect'));
  const [identifier, setIdentifier] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(ERRORS[params.get('error') ?? ''] ?? null);
  const [pending, setPending] = useState(false);

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    setPending(true);
    setError(null);
    try {
      const res = await authApi.login(identifier.trim(), password);
      if (res.result === 'DORMANT') {
        navigate(`/auth/dormant?redirect=${encodeURIComponent(redirect)}`);
        return;
      }
      await refresh();
      navigate(redirect, { replace: true });
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setPending(false);
    }
  };

  return (
    <div className="auth-wrap">
      <div className="auth-card">
        <Link to="/" className="brand" style={{ alignSelf: 'center' }}>
          <img src="/logo.png" alt="" />
          <span>
            이벤<em>터</em>
          </span>
        </Link>
        <h1 style={{ textAlign: 'center' }}>다시 만나서 반가워요</h1>
        <p className="sub" style={{ textAlign: 'center' }}>
          로그인하고 추천·댓글·찜을 이용해 보세요
        </p>

        <a href={authApi.googleLoginUrl(redirect)} className="btn btn-google btn-lg btn-block">
          <Icon name="google" size={20} /> Google 계정으로 계속하기
        </a>
        <p className="help" style={{ margin: '-6px 0 0', textAlign: 'center' }}>
          카카오·네이버로 가입하셨다면 같은 이메일의 구글 계정으로 로그인하면 기존 계정과 연결돼요.
        </p>

        <div className="divider">또는 아이디로 로그인</div>

        <form className="stack" onSubmit={submit}>
          {error && <div className="alert alert-error">{error}</div>}
          <div className="field">
            <label htmlFor="identifier">아이디</label>
            <input
              id="identifier"
              className="input"
              autoComplete="username"
              value={identifier}
              onChange={(e) => setIdentifier(e.target.value)}
              required
            />
          </div>
          <div className="field">
            <label htmlFor="password">비밀번호</label>
            <input
              id="password"
              className="input"
              type="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <button className="btn btn-primary btn-lg btn-block" disabled={pending || !identifier || !password}>
            {pending ? '로그인 중…' : '로그인'}
          </button>
        </form>
        <div className="row-between" style={{ justifyContent: 'center', gap: 16, fontSize: 14 }}>
          <Link to="/auth/signup" style={{ fontWeight: 700 }}>회원가입</Link>
          <span className="muted">|</span>
          <Link to="/auth/find" className="muted">아이디·비밀번호 찾기</Link>
        </div>
        <p className="help" style={{ margin: 0, textAlign: 'center' }}>
          처음 로그인하면 <Link to="/terms/2" style={{ textDecoration: 'underline' }}>서비스 이용약관</Link>과{' '}
          <Link to="/terms/1" style={{ textDecoration: 'underline' }}>개인정보처리방침</Link>에 동의하는 것으로 봅니다.
        </p>
      </div>
    </div>
  );
}

export function DormantPage() {
  useDocumentTitle('휴면 해제');
  const [params] = useSearchParams();
  const navigate = useNavigate();
  const toast = useToast();
  const { refresh } = useAuth();
  const [pending, setPending] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const activate = async () => {
    setPending(true);
    setError(null);
    try {
      const res = await authApi.activateDormant();
      toast(res.message);
      await refresh();
      navigate(safeRedirect(params.get('redirect')), { replace: true });
    } catch (e) {
      setError((e as Error).message);
    } finally {
      setPending(false);
    }
  };

  return (
    <div className="auth-wrap">
      <div className="auth-card">
        <div style={{ fontSize: 44, textAlign: 'center' }} aria-hidden="true">
          😴
        </div>
        <h1 style={{ textAlign: 'center' }}>휴면 상태인 계정이에요</h1>
        <p className="sub" style={{ textAlign: 'center' }}>
          오랫동안 로그인하지 않아 휴면으로 바뀌었어요. 해제하면 바로 다시 이용할 수 있어요.
        </p>
        {error && (
          <div className="alert alert-error">
            {error} <Link to="/auth/login">다시 로그인</Link>
          </div>
        )}
        <button className="btn btn-primary btn-lg btn-block" onClick={activate} disabled={pending}>
          {pending ? '해제 중…' : '휴면 해제하고 계속하기'}
        </button>
        <Link to="/" className="btn btn-ghost btn-block">
          나중에 할게요
        </Link>
      </div>
    </div>
  );
}

export function GoogleSignupPage() {
  useDocumentTitle('회원가입');
  const navigate = useNavigate();
  const toast = useToast();
  const { refresh } = useAuth();
  const [nickname, setNickname] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [pending, setPending] = useState(false);
  const pendingInfo = useQuery({ queryKey: ['google', 'pending'], queryFn: authApi.googlePending, retry: false });

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    setPending(true);
    setError(null);
    try {
      const res = await authApi.googleSignup(nickname.trim());
      toast(res.message);
      await refresh();
      navigate('/', { replace: true });
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setPending(false);
    }
  };

  if (pendingInfo.isError) {
    return (
      <div className="auth-wrap">
        <div className="auth-card">
          <h1>가입 시간이 지났어요</h1>
          <p className="sub">구글 로그인을 다시 진행해 주세요.</p>
          <Link to="/auth/login" className="btn btn-primary btn-lg btn-block">
            로그인으로
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="auth-wrap">
      <div className="auth-card">
        <h1>거의 다 됐어요!</h1>
        <p className="sub">이벤터에서 쓸 닉네임을 정해 주세요.</p>
        {pendingInfo.data?.email && (
          <div className="alert" style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <Icon name="google" size={18} /> {pendingInfo.data.email}
          </div>
        )}
        <form className="stack" onSubmit={submit}>
          {error && <div className="alert alert-error">{error}</div>}
          <div className="field">
            <label htmlFor="nickname">닉네임</label>
            <input
              id="nickname"
              className="input"
              value={nickname}
              onChange={(e) => setNickname(e.target.value)}
              minLength={2}
              maxLength={10}
              placeholder="2~10자"
              required
              autoFocus
            />
            <span className="help">닉네임은 한 달에 한 번 바꿀 수 있어요.</span>
          </div>
          <p className="help" style={{ margin: 0 }}>
            가입하면 이벤터의 <Link to="/terms/2" target="_blank" style={{ textDecoration: 'underline' }}>서비스 이용약관</Link>과{' '}
            <Link to="/terms/1" target="_blank" style={{ textDecoration: 'underline' }}>개인정보처리방침</Link>에 동의하는 것으로 봅니다.
          </p>
          <button className="btn btn-primary btn-lg btn-block" disabled={pending || nickname.trim().length < 2}>
            {pending ? '가입 중…' : '동의하고 가입 완료'}
          </button>
        </form>
      </div>
    </div>
  );
}
