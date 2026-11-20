import { Link } from 'react-router';
import privacyHtml from '@/content/privacy.html?raw';
import privacyToc from '@/content/privacy.toc.json';
import termsHtml from '@/content/terms.html?raw';
import termsToc from '@/content/terms.toc.json';
import { useDocumentTitle } from '@/lib/useDocumentTitle';

// 개인정보처리방침·서비스 이용약관. 문구는 옛 사이트(templates/term/policy1·2.html)와 글자 하나 다르지 않게 옮겼다.
// 고칠 때는 src/content/*.html 을 고친다(시행일·개정 이력도 함께).
const DOCS = {
  privacy: { title: '개인정보처리방침', html: privacyHtml, toc: privacyToc },
  terms: { title: '서비스 이용약관', html: termsHtml, toc: termsToc },
} as const;

export function PolicyPage({ doc }: { doc: keyof typeof DOCS }) {
  const { title, html, toc } = DOCS[doc];
  useDocumentTitle(title);

  return (
    <div className="page">
      <div className="container">
        <div className="tabs" style={{ marginBottom: 20 }}>
          <Link to="/terms/1" className={doc === 'privacy' ? 'active' : ''}>
            개인정보처리방침
          </Link>
          <Link to="/terms/2" className={doc === 'terms' ? 'active' : ''}>
            서비스 이용약관
          </Link>
        </div>
        <div className="policy-layout">
          <article className="panel policy" dangerouslySetInnerHTML={{ __html: html }} />
          <nav className="policy-toc hide-mobile" aria-label="목차">
            <strong>목차</strong>
            <ul>
              {toc.map((item) => (
                <li key={item.id}>
                  <a href={`#${item.id}`}>{item.title}</a>
                </li>
              ))}
            </ul>
          </nav>
        </div>
      </div>
    </div>
  );
}
