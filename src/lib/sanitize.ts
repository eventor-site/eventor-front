import DOMPurify from 'dompurify';

// 게시물 본문 HTML 정화(XSS 방지). 유튜브 등 영상 iframe 만 허용한다.
const ALLOWED_IFRAME = /^https:\/\/(www\.)?(youtube\.com|youtube-nocookie\.com|player\.vimeo\.com|tv\.naver\.com)\//;

DOMPurify.addHook('uponSanitizeElement', (node, data) => {
  if (data.tagName === 'iframe') {
    const src = (node as Element).getAttribute('src') || '';
    if (!ALLOWED_IFRAME.test(src)) node.parentNode?.removeChild(node);
  }
});

DOMPurify.addHook('afterSanitizeAttributes', (node) => {
  if (node.tagName === 'A') {
    node.setAttribute('target', '_blank');
    node.setAttribute('rel', 'noopener noreferrer nofollow');
  }
  if (node.tagName === 'IMG') {
    node.setAttribute('loading', 'lazy');
    node.removeAttribute('contenteditable');
  }
});

export function sanitizeHtml(html: string): string {
  return DOMPurify.sanitize(html, {
    ADD_TAGS: ['iframe'],
    ADD_ATTR: ['allow', 'allowfullscreen', 'frameborder', 'target'],
    FORBID_ATTR: ['style'],
  });
}
