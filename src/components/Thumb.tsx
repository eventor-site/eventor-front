import { useState } from 'react';
import { Icon } from './Icon';

/** 썸네일(이미지·영상). 없거나 깨지면 자리표시 */
export function Thumb({ src, type, alt }: { src: string | null | undefined; type?: string | null; alt: string }) {
  const [broken, setBroken] = useState(false);
  if (!src || broken) {
    return (
      <div className="thumb-empty">
        <Icon name="gift" size={28} />
      </div>
    );
  }
  if (type === 'video') {
    return <video src={src} muted playsInline preload="metadata" />;
  }
  return <img src={src} alt={alt} loading="lazy" decoding="async" onError={() => setBroken(true)} />;
}
