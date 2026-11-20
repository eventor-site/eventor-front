import { useQuery } from '@tanstack/react-query';
import { useState } from 'react';
import { reportApi } from '@/api/endpoints';
import { Modal } from './Modal';
import { useToast } from './Toast';

/** 게시물·댓글 신고 */
export function ReportDialog({ target, id, onClose }: { target: 'post' | 'comment'; id: number; onClose: () => void }) {
  const toast = useToast();
  const types = useQuery({ queryKey: ['reportTypes'], queryFn: reportApi.types, staleTime: 10 * 60_000 });
  const [selected, setSelected] = useState('');
  const [busy, setBusy] = useState(false);

  const submit = async () => {
    setBusy(true);
    try {
      const res = target === 'post' ? await reportApi.post(id, selected) : await reportApi.comment(id, selected);
      toast(res.message ?? '신고했어요.');
      onClose();
    } catch (e) {
      toast((e as Error).message, 'error');
    } finally {
      setBusy(false);
    }
  };

  return (
    <Modal title={target === 'post' ? '게시물 신고' : '댓글 신고'} onClose={onClose}>
      <p className="help" style={{ margin: 0 }}>신고 사유를 골라 주세요. 운영진이 확인 후 조치해요.</p>
      <div className="stack" style={{ gap: 6 }}>
        {types.data?.map((t) => (
          <label key={t.reportTypeId} className="check radio-row">
            <input type="radio" name="reportType" value={t.name} checked={selected === t.name} onChange={() => setSelected(t.name)} />
            <span>{t.name}</span>
          </label>
        ))}
      </div>
      <button className="btn btn-primary btn-block" disabled={!selected || busy} onClick={submit}>
        신고하기
      </button>
    </Modal>
  );
}
