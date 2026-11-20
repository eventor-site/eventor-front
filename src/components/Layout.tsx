import { useEffect, useRef, useState, type FormEvent } from 'react';
import { Link, NavLink, Outlet, useLocation, useNavigate, useSearchParams } from 'react-router';
import { useAuth } from '@/auth/AuthContext';
import { categoryHref, useCategories } from '@/lib/useCategories';
import { Icon } from './Icon';

function SearchBox({ large, autoFocus }: { large?: boolean; autoFocus?: boolean }) {
  const navigate = useNavigate();
  const [params] = useSearchParams();
  const location = useLocation();
  const [value, setValue] = useState(location.pathname === '/search' ? params.get('q') ?? '' : '');

  useEffect(() => {
    if (location.pathname === '/search') setValue(params.get('q') ?? '');
  }, [location.pathname, params]);

  const submit = (e: FormEvent) => {
    e.preventDefault();
    const q = value.trim();
    if (q) navigate(`/search?q=${encodeURIComponent(q)}`);
  };

  return (
    <form className={`search-box ${large ? 'lg' : ''}`} onSubmit={submit} role="search">
      <Icon name="search" size={18} />
      <input
        type="search"
        value={value}
        onChange={(e) => setValue(e.target.value)}
        placeholder="이벤트, 핫딜 상품을 검색해 보세요"
        aria-label="검색어"
        autoFocus={autoFocus}
        enterKeyHint="search"
      />
    </form>
  );
}

export { SearchBox };

function MegaMenu({ onClose }: { onClose: () => void }) {
  const { data } = useCategories();
  const event = data?.find((c) => c.name === '이벤트');
  if (!event) return null;
  return (
    <div className="mega" onMouseLeave={onClose}>
      <div className="container">
        <div className="mega-grid">
          {event.children.map((group) => (
            <div key={group.categoryId}>
              <h5>
                <Link to={categoryHref(group.name)} onClick={onClose}>
                  {group.name}
                </Link>
              </h5>
              <ul>
                {group.children.map((c) => (
                  <li key={c.categoryId}>
                    <Link to={categoryHref(c.name)} onClick={onClose}>
                      {c.name}
                    </Link>
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

function Drawer({ onClose }: { onClose: () => void }) {
  const { data } = useCategories();
  const { isLoggedIn, logout } = useAuth();
  return (
    <>
      <div className="drawer-backdrop" onClick={onClose} />
      <aside className="drawer" aria-label="전체 메뉴">
        <div className="row-between">
          <Link to="/" className="brand" onClick={onClose}>
            <img src="/logo.png" alt="" />
            <span>
              이벤<em>터</em>
            </span>
          </Link>
          <button className="icon-btn" onClick={onClose} aria-label="닫기">
            <Icon name="close" />
          </button>
        </div>
        {data?.map((top) => (
          <div className="cat-group" key={top.categoryId}>
            <h4>
              <Link to={categoryHref(top.name)} onClick={onClose}>
                {top.name}
              </Link>
            </h4>
            {top.children.map((group) => (
              <div key={group.categoryId} style={{ marginBottom: 10 }}>
                <div className="cat-links">
                  <Link to={categoryHref(group.name)} className="chip chip-sm" onClick={onClose}>
                    <strong>{group.name}</strong>
                  </Link>
                  {group.children.map((c) => (
                    <Link key={c.categoryId} to={categoryHref(c.name)} className="chip chip-sm" onClick={onClose}>
                      {c.name}
                    </Link>
                  ))}
                </div>
              </div>
            ))}
          </div>
        ))}
        <div className="cat-group">
          <h4>축제</h4>
          <div className="cat-links">
            <Link to="/tours/festival2" className="chip chip-sm" onClick={onClose}>전국 축제</Link>
            <Link to="/insights" className="chip chip-sm" onClick={onClose}>혜택 트렌드</Link>
          </div>
        </div>
        <div className="cat-group">
          {isLoggedIn ? (
            <button
              className="btn btn-outline btn-block"
              onClick={() => {
                void logout();
                onClose();
              }}
            >
              <Icon name="logout" size={18} /> 로그아웃
            </button>
          ) : (
            <Link to="/auth/login" className="btn btn-primary btn-block" onClick={onClose}>
              로그인
            </Link>
          )}
        </div>
      </aside>
    </>
  );
}

function UserMenu() {
  const { isLoggedIn, isAdmin, logout } = useAuth();
  const location = useLocation();
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const close = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) setOpen(false);
    };
    document.addEventListener('click', close);
    return () => document.removeEventListener('click', close);
  }, []);

  if (!isLoggedIn) {
    const redirect = encodeURIComponent(location.pathname + location.search);
    return (
      <Link to={`/auth/login?redirect=${redirect}`} className="btn btn-primary btn-sm">
        로그인
      </Link>
    );
  }
  return (
    <div ref={ref} style={{ position: 'relative' }}>
      <button className="icon-btn" onClick={() => setOpen((v) => !v)} aria-label="내 메뉴" aria-expanded={open}>
        <Icon name="user" />
      </button>
      {open && (
        <div
          className="panel"
          style={{ position: 'absolute', right: 0, top: 46, width: 180, padding: 8, boxShadow: 'var(--shadow)' }}
        >
          <Link to="/me" className="btn btn-ghost btn-block" style={{ justifyContent: 'flex-start' }}>
            <Icon name="user" size={18} /> 마이페이지
          </Link>
          <Link to="/write" className="btn btn-ghost btn-block" style={{ justifyContent: 'flex-start' }}>
            <Icon name="edit" size={18} /> 글쓰기
          </Link>
          {isAdmin && (
            <Link to="/admin" className="btn btn-ghost btn-block" style={{ justifyContent: 'flex-start' }}>
              <Icon name="bookmark" size={18} /> 관리자
            </Link>
          )}
          <button
            className="btn btn-ghost btn-block"
            style={{ justifyContent: 'flex-start' }}
            onClick={() => {
              setOpen(false);
              void logout();
            }}
          >
            <Icon name="logout" size={18} /> 로그아웃
          </button>
        </div>
      )}
    </div>
  );
}

export function Layout() {
  const location = useLocation();
  const [mega, setMega] = useState(false);
  const [drawer, setDrawer] = useState(false);
  const { isLoggedIn } = useAuth();
  const params = new URLSearchParams(location.search);
  const categoryName = params.get('categoryName');

  useEffect(() => {
    setMega(false);
    setDrawer(false);
    window.scrollTo({ top: 0 });
  }, [location.pathname, location.search]);

  const navActive = (name: string) => location.pathname === '/posts' && categoryName === name;

  return (
    <>
      <header className="header">
        <div className="container header-inner">
          <button className="icon-btn hide-desktop" onClick={() => setDrawer(true)} aria-label="전체 메뉴">
            <Icon name="menu" />
          </button>
          <Link to="/" className="brand" aria-label="이벤터 홈">
            <img src="/logo.png" alt="" />
            <span>
              이벤<em>터</em>
            </span>
          </Link>
          <nav className="nav" aria-label="주 메뉴">
            <Link
              to={categoryHref('이벤트')}
              className={navActive('이벤트') ? 'active' : ''}
              onMouseEnter={() => setMega(true)}
            >
              이벤트
            </Link>
            <Link to={categoryHref('핫딜')} className={navActive('핫딜') ? 'active' : ''} onMouseEnter={() => setMega(false)}>
              핫딜
            </Link>
            <Link to={categoryHref('자유')} className={navActive('자유') ? 'active' : ''} onMouseEnter={() => setMega(false)}>
              커뮤니티
            </Link>
            <Link to="/tours/festival2" className={location.pathname.startsWith('/tours') ? 'active' : ''} onMouseEnter={() => setMega(false)}>
              축제
            </Link>
            <Link to="/insights" className={location.pathname.startsWith('/insights') || location.pathname.startsWith('/brands') ? 'active' : ''} onMouseEnter={() => setMega(false)}>
              트렌드
            </Link>
            <Link to={categoryHref('공지')} className={navActive('공지') ? 'active' : ''} onMouseEnter={() => setMega(false)}>
              공지
            </Link>
          </nav>
          <div className="header-search">
            <SearchBox />
          </div>
          <div className="header-actions">
            <Link to="/search" className="icon-btn hide-desktop" aria-label="검색">
              <Icon name="search" />
            </Link>
            <UserMenu />
          </div>
        </div>
        {mega && <MegaMenu onClose={() => setMega(false)} />}
      </header>

      {drawer && <Drawer onClose={() => setDrawer(false)} />}

      <main>
        <Outlet />
      </main>

      <footer className="footer">
        <div className="container">
          <div className="footer-grid">
            <div>
              <div className="brand" style={{ fontSize: 16 }}>
                <img src="/logo.png" alt="" style={{ width: 24, height: 24 }} />
                <span>
                  이벤<em>터</em>
                </span>
              </div>
              <p style={{ margin: '8px 0 0' }}>
                광고 문의 이메일: <a href="mailto:eventor2025@naver.com">eventor2025@naver.com</a>
              </p>
              <div className="footer-social">
                <a href="https://blog.naver.com/eventor2025" target="_blank" rel="noopener noreferrer" aria-label="네이버 블로그" title="네이버 블로그">
                  B
                </a>
                <a href="https://www.instagram.com/eventor_2025/" target="_blank" rel="noopener noreferrer" aria-label="인스타그램" title="인스타그램">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" aria-hidden="true"><rect x="3" y="3" width="18" height="18" rx="5" /><circle cx="12" cy="12" r="4" /><circle cx="17.5" cy="6.5" r="1" fill="currentColor" /></svg>
                </a>
                <a href="https://www.youtube.com/@Eventor2025" target="_blank" rel="noopener noreferrer" aria-label="유튜브" title="유튜브">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true"><path d="M23 7.2a3 3 0 0 0-2.1-2.1C19 4.6 12 4.6 12 4.6s-7 0-8.9.5A3 3 0 0 0 1 7.2 31 31 0 0 0 .5 12a31 31 0 0 0 .5 4.8 3 3 0 0 0 2.1 2.1c1.9.5 8.9.5 8.9.5s7 0 8.9-.5a3 3 0 0 0 2.1-2.1 31 31 0 0 0 .5-4.8 31 31 0 0 0-.5-4.8zM9.8 15.1V8.9l5.4 3.1z" /></svg>
                </a>
                <a href="https://play.google.com/store/apps/details?id=com.eventorapp" target="_blank" rel="noopener noreferrer" aria-label="구글 플레이스토어" title="구글 플레이스토어">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true"><path d="M4 2.5v19l10-9.5zm11.3 8.3 2.6-2.5L6 1.8zm0 2.4L6 22.2l11.9-6.5zm4.4-3.6-2.9-1.6-2.8 2.7 2.8 2.7 2.9-1.6a1.3 1.3 0 0 0 0-2.2z" /></svg>
                </a>
              </div>
            </div>
            <nav className="footer-policy" aria-label="정책">
              <Link to="/terms/1" className="strong">
                개인정보처리방침
              </Link>
              <Link to="/terms/2">서비스 이용약관</Link>
            </nav>
          </div>
          <div>Copyright © 2025 Eventor. All Rights Reserved.</div>
        </div>
      </footer>

      <nav className="tabbar" aria-label="하단 메뉴">
        <NavLink to="/" end>
          <Icon name="home" size={22} />홈
        </NavLink>
        <Link to={categoryHref('이벤트')} className={navActive('이벤트') ? 'active' : ''}>
          <Icon name="gift" size={22} />
          이벤트
        </Link>
        <Link to={categoryHref('핫딜')} className={navActive('핫딜') ? 'active' : ''}>
          <Icon name="flame" size={22} />
          핫딜
        </Link>
        <NavLink to="/search">
          <Icon name="search" size={22} />
          검색
        </NavLink>
        <NavLink to={isLoggedIn ? '/me' : '/auth/login'}>
          <Icon name="user" size={22} />
          {isLoggedIn ? 'MY' : '로그인'}
        </NavLink>
      </nav>
    </>
  );
}
