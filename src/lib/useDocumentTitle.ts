import { useEffect } from 'react';

const SITE = '이벤터';

export function useDocumentTitle(title?: string | null) {
  useEffect(() => {
    document.title = title ? `${title} | ${SITE}` : `${SITE} - 이벤트·핫딜 모아보기`;
  }, [title]);
}
