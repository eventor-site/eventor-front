import { useQuery } from '@tanstack/react-query';
import { useState } from 'react';
import { Link, useParams, useSearchParams } from 'react-router';
import { tourApi } from '@/api/endpoints';
import type { FestivalItem } from '@/api/types';
import { CardSkeletons, Empty, ErrorBox } from '@/components/Feedback';
import { Icon } from '@/components/Icon';
import { Dropdown } from '@/components/Dropdown';
import { Thumb } from '@/components/Thumb';
import { useDocumentTitle } from '@/lib/useDocumentTitle';

// 한국관광공사 TourAPI 기반 전국 축제(옛 사이트 /tours/festival2 와 같은 주소).
// 주변 관광지·맛집·숙소(/tours)는 지오코더 API 종료로 2026-11-20 제거, 옛 주소는 축제 목록으로 넘긴다.

export function FestivalCard({ f }: { f: FestivalItem }) {
  const badge =
    f.eventStatusName === '진행중' ? <span className="badge badge-hot">진행중</span>
      : f.eventStatusName === '예정' ? <span className="badge badge-blue">{f.remainingDay ? `${f.remainingDay}일 후` : '예정'}</span>
        : <span className="badge badge-dark">{f.eventStatusName}</span>;
  return (
    <Link to={`/tours/${f.contentId}?contentTypeId=${f.contentTypeId}`} className="card">
      <div className="thumb" style={{ aspectRatio: '4 / 3' }}>
        <Thumb src={f.firstImage} alt={f.title} />
        <div className="thumb-badges">{badge}</div>
      </div>
      <div className="card-body">
        <h3 className="card-title">{f.title}</h3>
        <div className="card-meta" style={{ flexDirection: 'column', alignItems: 'flex-start', gap: 2 }}>
          <span>{f.eventStartDate} ~ {f.eventEndDate}</span>
          {f.addr1 && <span className="meta-item"><Icon name="map" size={13} /> {f.addr1.split(' ').slice(0, 2).join(' ')}</span>}
        </div>
      </div>
    </Link>
  );
}

export function FestivalPage() {
  useDocumentTitle('전국 축제');
  const [status, setStatus] = useState<'' | '진행중' | '예정'>('');
  const [region, setRegion] = useState('');
  const q = useQuery({ queryKey: ['festivals'], queryFn: tourApi.festivals, staleTime: 10 * 60_000 });
  const regions = Array.from(new Set((q.data ?? []).map((f) => f.addr1?.split(' ')[0]).filter(Boolean))) as string[];
  const list = (q.data ?? []).filter((f) => (!status || f.eventStatusName === status) && (!region || f.addr1?.startsWith(region)));

  return (
    <div className="page">
      <div className="container">
        <div className="row-between">
          <h1 className="page-title">전국 축제</h1>
          <span className="muted">{list.length}개</span>
        </div>
        <p className="section-desc">앞으로 석 달 동안 열리는 축제예요. 정보 제공: 한국관광공사</p>
        <div className="toolbar">
          <div className="chips">
            {(['', '진행중', '예정'] as const).map((s) => (
              <button key={s || 'all'} className={`chip chip-sm ${status === s ? 'active' : ''}`} onClick={() => setStatus(s)}>{s || '전체'}</button>
            ))}
          </div>
          <Dropdown label="지역" value={region} onChange={setRegion} icon={<Icon name="map" size={15} />}
            options={[{ value: '', label: '전국', hint: `${(q.data ?? []).length}개 축제` },
              ...regions.sort().map((r) => ({ value: r, label: r, hint: `${(q.data ?? []).filter((f) => f.addr1?.startsWith(r)).length}개` }))]} />
        </div>
        {q.isError ? <ErrorBox error={q.error} onRetry={() => q.refetch()} />
          : !q.data ? <div className="grid"><CardSkeletons count={8} /></div>
            : list.length === 0 ? <Empty title="조건에 맞는 축제가 없어요" />
              : <div className="grid">{list.map((f) => <FestivalCard key={f.contentId} f={f} />)}</div>}
      </div>
    </div>
  );
}

export function TourDetailPage() {
  const { contentId = '' } = useParams();
  const [params] = useSearchParams();
  const contentTypeId = params.get('contentTypeId') ?? '15';
  const q = useQuery({ queryKey: ['tour', contentId, contentTypeId], queryFn: () => tourApi.detail(contentId, contentTypeId) });
  useDocumentTitle(q.data?.title);
  const t = q.data;
  const rows: [string, string | null | undefined][] = t ? [
    ['주소', t.address], ['문의', t.tel || t.infoCenter], ['이용 시간', t.useTime], ['쉬는 날', t.restDate],
    ['입장료', t.admission], ['주차', t.parking],
  ] : [];
  return (
    <div className="page">
      <div className="container" style={{ maxWidth: 900 }}>
        <nav className="breadcrumb"><Link to="/tours/festival2">전국 축제</Link></nav>
        {q.isError ? <ErrorBox error={q.error} onRetry={() => q.refetch()} /> : !t ? <div className="skeleton" style={{ height: 360 }} /> : (
          <article className="stack" style={{ gap: 18 }}>
            <h1 className="article-title" style={{ margin: 0 }}>{t.title}</h1>
            {t.images.length > 0 && (
              <div className="rail" style={{ gridAutoColumns: 'minmax(260px, 80%)' }}>
                {t.images.slice(0, 10).map((src) => (
                  <div key={src} className="thumb" style={{ aspectRatio: '4 / 3', borderRadius: 14 }}><img src={src} alt={t.title} loading="lazy" /></div>
                ))}
              </div>
            )}
            <dl className="panel stack" style={{ gap: 10, margin: 0 }}>
              {rows.filter(([, v]) => v).map(([k, v]) => (
                <div key={k} className="info-row"><dt>{k}</dt><dd dangerouslySetInnerHTML={{ __html: (v as string).replace(/<(?!br\s*\/?)[^>]*>/gi, '') }} /></div>
              ))}
              {t.mapX && t.mapY && (
                <a className="btn btn-outline btn-block" target="_blank" rel="noopener noreferrer"
                  href={`https://map.kakao.com/link/map/${encodeURIComponent(t.title)},${t.mapY},${t.mapX}`}>
                  <Icon name="map" size={18} /> 지도에서 보기
                </a>
              )}
            </dl>
            {t.overview && <div className="prose" style={{ padding: 0 }}>{t.overview.replace(/<[^>]*>/g, '')}</div>}
            <p className="help">정보 제공: 한국관광공사. 일정과 내용은 바뀔 수 있으니 방문 전 확인해 주세요.</p>
          </article>
        )}
      </div>
    </div>
  );
}
