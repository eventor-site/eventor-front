import { useState, type FormEvent } from 'react';
import { Link, useNavigate } from 'react-router';
import { authApi, signupApi } from '@/api/endpoints';
import { Icon } from '@/components/Icon';
import { useToast } from '@/components/Toast';
import { useDocumentTitle } from '@/lib/useDocumentTitle';

const PASSWORD_RULE = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+]).{8,}$/;
const IDENTIFIER_RULE = /^[a-zA-Z0-9]{4,20}$/;
const OK = (msg: string | null | undefined) => !!msg && (msg.startsWith('사용 가능') || msg.startsWith('인증'));

export function SignupPage() {
  useDocumentTitle('회원가입');
  const navigate = useNavigate();
  const toast = useToast();
  const [identifier, setIdentifier] = useState('');
  const [idMsg, setIdMsg] = useState<string | null>(null);
  const [password, setPassword] = useState('');
  const [password2, setPassword2] = useState('');
  const [nickname, setNickname] = useState('');
  const [nickMsg, setNickMsg] = useState<string | null>(null);
  const [email, setEmail] = useState('');
  const [code, setCode] = useState('');
  const [codeSent, setCodeSent] = useState(false);
  const [verified, setVerified] = useState(false);
  const [agree, setAgree] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [busy, setBusy] = useState(false);

  const checkId = async () => {
    if (!IDENTIFIER_RULE.test(identifier)) return setIdMsg('아이디는 영문·숫자 4~20자로 입력해 주세요.');
    setIdMsg((await signupApi.checkIdentifier(identifier).catch((e) => ({ message: (e as Error).message }))).message);
  };
  const checkNick = async () => {
    const n = nickname.trim();
    if (n.length < 2 || n.length > 10) return setNickMsg('닉네임은 2~10자로 입력해 주세요.');
    setNickMsg((await signupApi.checkNickname(n).catch((e) => ({ message: (e as Error).message }))).message);
  };
  const sendCode = async () => {
    if (!/^\S+@\S+\.\S+$/.test(email)) return toast('이메일 주소를 확인해 주세요.', 'error');
    try {
      const res = await signupApi.sendCode(email.trim());
      toast(res.message);
      setCodeSent(true);
    } catch (e) {
      toast((e as Error).message, 'error');
    }
  };
  const verify = async () => {
    try {
      const res = await signupApi.certify(email.trim(), code.trim());
      toast(res.message, res.data ? 'info' : 'error');
      setVerified(!!res.data);
    } catch (e) {
      toast((e as Error).message, 'error');
    }
  };

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    if (!OK(idMsg)) return setError('아이디 중복 확인을 해 주세요.');
    if (!PASSWORD_RULE.test(password)) return setError('비밀번호는 영문·숫자·특수문자를 포함해 8자 이상이어야 해요.');
    if (password !== password2) return setError('비밀번호가 서로 달라요.');
    if (!OK(nickMsg)) return setError('닉네임 중복 확인을 해 주세요.');
    if (!verified) return setError('이메일 인증을 완료해 주세요.');
    if (!agree) return setError('약관에 동의해 주세요.');
    setBusy(true);
    try {
      const res = await signupApi.signup({ identifier, password, nickname: nickname.trim(), email: email.trim() });
      toast(res.message ?? '가입했어요. 로그인해 주세요.');
      navigate('/auth/login', { replace: true });
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className="auth-wrap">
      <form className="auth-card" onSubmit={submit} style={{ maxWidth: 480 }}>
        <h1>회원가입</h1>
        <p className="sub">구글 계정이 있다면 더 간단하게 시작할 수 있어요.</p>
        <a href={authApi.googleLoginUrl('/')} className="btn btn-google btn-lg btn-block">
          <Icon name="google" size={20} /> Google 계정으로 가입
        </a>
        <div className="divider">또는 아이디로 가입</div>
        {error && <div className="alert alert-error">{error}</div>}

        <div className="field">
          <label htmlFor="su-id">아이디</label>
          <div className="input-row">
            <input id="su-id" className="input" value={identifier} autoComplete="username"
              onChange={(e) => { setIdentifier(e.target.value); setIdMsg(null); }} placeholder="영문·숫자 4~20자" />
            <button type="button" className="btn btn-outline" onClick={checkId}>중복 확인</button>
          </div>
          {idMsg && <span className={OK(idMsg) ? 'help ok-text' : 'error-text'}>{idMsg}</span>}
        </div>
        <div className="field">
          <label htmlFor="su-pw">비밀번호</label>
          <input id="su-pw" className="input" type="password" autoComplete="new-password" value={password} onChange={(e) => setPassword(e.target.value)} />
          <span className={password && !PASSWORD_RULE.test(password) ? 'error-text' : 'help'}>영문·숫자·특수문자(!@#$%^&*()_+)를 포함해 8자 이상</span>
        </div>
        <div className="field">
          <label htmlFor="su-pw2">비밀번호 확인</label>
          <input id="su-pw2" className="input" type="password" autoComplete="new-password" value={password2} onChange={(e) => setPassword2(e.target.value)} />
          {password2 && password !== password2 && <span className="error-text">비밀번호가 서로 달라요.</span>}
        </div>
        <div className="field">
          <label htmlFor="su-nick">닉네임</label>
          <div className="input-row">
            <input id="su-nick" className="input" value={nickname} onChange={(e) => { setNickname(e.target.value); setNickMsg(null); }} maxLength={10} placeholder="2~10자" />
            <button type="button" className="btn btn-outline" onClick={checkNick}>중복 확인</button>
          </div>
          {nickMsg && <span className={OK(nickMsg) ? 'help ok-text' : 'error-text'}>{nickMsg}</span>}
        </div>
        <div className="field">
          <label htmlFor="su-email">이메일</label>
          <div className="input-row">
            <input id="su-email" className="input" type="email" value={email} disabled={verified}
              onChange={(e) => { setEmail(e.target.value); setVerified(false); }} autoComplete="email" />
            <button type="button" className="btn btn-outline" onClick={sendCode} disabled={verified}>
              {codeSent ? '다시 받기' : '인증번호 받기'}
            </button>
          </div>
          {codeSent && !verified && (
            <div className="input-row">
              <input className="input" inputMode="numeric" value={code} onChange={(e) => setCode(e.target.value)} placeholder="인증번호 6자리" maxLength={6} />
              <button type="button" className="btn btn-primary" onClick={verify} disabled={code.length < 6}>확인</button>
            </div>
          )}
          {verified && <span className="help ok-text">이메일 인증을 마쳤어요.</span>}
        </div>
        <label className="check">
          <input type="checkbox" checked={agree} onChange={(e) => setAgree(e.target.checked)} />
          <span>
            <Link to="/terms/2" target="_blank">서비스 이용약관</Link>과 <Link to="/terms/1" target="_blank">개인정보처리방침</Link>에 동의합니다. (필수)
          </span>
        </label>
        <button className="btn btn-primary btn-lg btn-block" disabled={busy}>
          {busy ? '가입 중…' : '가입하기'}
        </button>
        <p className="help" style={{ textAlign: 'center', margin: 0 }}>
          이미 계정이 있나요? <Link to="/auth/login" style={{ textDecoration: 'underline', display: 'inline-block', padding: '10px 6px' }}>로그인</Link>
        </p>
      </form>
    </div>
  );
}

export function FindAccountPage() {
  useDocumentTitle('계정 찾기');
  const toast = useToast();
  const navigate = useNavigate();
  const [mode, setMode] = useState<'id' | 'password'>('password');
  const [email, setEmail] = useState('');
  const [identifier, setIdentifier] = useState('');
  const [code, setCode] = useState('');
  const [newPw, setNewPw] = useState('');
  const [sent, setSent] = useState(false);
  const [message, setMessage] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const run = async (fn: () => Promise<{ message: string | null }>, after?: () => void) => {
    setError(null);
    try {
      const res = await fn();
      setMessage(res.message);
      after?.();
    } catch (e) {
      setError((e as Error).message);
    }
  };

  return (
    <div className="auth-wrap">
      <div className="auth-card">
        <h1>계정 찾기</h1>
        <div className="tabs">
          <button className={mode === 'password' ? 'active' : ''} onClick={() => { setMode('password'); setMessage(null); setError(null); }}>비밀번호 재설정</button>
          <button className={mode === 'id' ? 'active' : ''} onClick={() => { setMode('id'); setMessage(null); setError(null); }}>아이디 찾기</button>
        </div>
        <p className="help" style={{ margin: 0 }}>
          구글·카카오·네이버로 가입한 계정은 비밀번호가 없어요. 구글 로그인을 이용해 주세요.
        </p>
        {error && <div className="alert alert-error">{error}</div>}
        {message && <div className="alert">{message}</div>}

        {mode === 'id' ? (
          <form className="stack" onSubmit={(e) => { e.preventDefault(); void run(() => signupApi.findIdentifier(email.trim())); }}>
            <div className="field">
              <label htmlFor="fa-email">가입한 이메일</label>
              <input id="fa-email" className="input" type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
            </div>
            <button className="btn btn-primary btn-lg btn-block">아이디를 메일로 받기</button>
          </form>
        ) : (
          <form className="stack" onSubmit={(e) => {
            e.preventDefault();
            if (!sent) void run(() => signupApi.sendResetCode(identifier.trim()), () => setSent(true));
            else void run(() => signupApi.resetPassword(identifier.trim(), code.trim(), newPw), () => {
              toast('비밀번호를 바꿨어요.');
              navigate('/auth/login', { replace: true });
            });
          }}>
            <div className="field">
              <label htmlFor="fa-id">아이디</label>
              <input id="fa-id" className="input" value={identifier} onChange={(e) => setIdentifier(e.target.value)} disabled={sent} required />
            </div>
            {sent && (
              <>
                <div className="field">
                  <label htmlFor="fa-code">인증번호</label>
                  <input id="fa-code" className="input" inputMode="numeric" maxLength={6} value={code} onChange={(e) => setCode(e.target.value)} required />
                </div>
                <div className="field">
                  <label htmlFor="fa-pw">새 비밀번호</label>
                  <input id="fa-pw" className="input" type="password" autoComplete="new-password" value={newPw} onChange={(e) => setNewPw(e.target.value)} required />
                  <span className="help">영문·숫자·특수문자를 포함해 8자 이상</span>
                </div>
              </>
            )}
            <button className="btn btn-primary btn-lg btn-block">{sent ? '비밀번호 바꾸기' : '인증번호 받기'}</button>
            {sent && (
              <button type="button" className="btn btn-ghost btn-block" onClick={() => void run(() => signupApi.sendResetCode(identifier.trim()))}>
                인증번호 다시 받기
              </button>
            )}
          </form>
        )}
        <Link to="/auth/login" className="btn btn-ghost btn-block">로그인으로</Link>
      </div>
    </div>
  );
}
