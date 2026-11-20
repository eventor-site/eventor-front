import Editor from '@toast-ui/editor';
import '@toast-ui/editor/dist/i18n/ko-kr';
import '@toast-ui/editor/dist/toastui-editor.css';
import '@toast-ui/editor/dist/theme/toastui-editor-dark.css';
import { forwardRef, useEffect, useImperativeHandle, useRef } from 'react';

export type RichEditorHandle = {
  getHtml: () => string;
  setHtml: (html: string) => void;
};

type Props = {
  initialHtml?: string;
  /** 붙여넣거나 끌어 놓은 이미지를 올리고 그 주소를 돌려준다 */
  onUploadImage: (file: File) => Promise<string>;
  height?: string;
};

const prefersDark = () => window.matchMedia?.('(prefers-color-scheme: dark)').matches;

/** Toast UI 위지윅 에디터(옛 사이트와 같은 에디터라 기존 글 HTML 을 그대로 고칠 수 있다) */
const RichEditor = forwardRef<RichEditorHandle, Props>(function RichEditor(
  { initialHtml = '', onUploadImage, height = '480px' },
  ref,
) {
  const el = useRef<HTMLDivElement>(null);
  const editor = useRef<Editor | null>(null);
  const uploadRef = useRef(onUploadImage);
  uploadRef.current = onUploadImage;

  useEffect(() => {
    if (!el.current) return;
    const instance = new Editor({
      el: el.current,
      height,
      initialEditType: 'wysiwyg',
      previewStyle: 'vertical',
      hideModeSwitch: true,
      language: 'ko-KR',
      theme: prefersDark() ? 'dark' : 'light',
      usageStatistics: false,
      placeholder: '내용을 입력하세요. 이미지는 붙여넣거나 끌어다 놓을 수 있어요.',
      toolbarItems: [
        ['heading', 'bold', 'italic', 'strike'],
        ['hr', 'quote'],
        ['ul', 'ol'],
        ['table', 'image', 'link'],
      ],
      hooks: {
        addImageBlobHook: async (blob: Blob | File, callback: (url: string, alt: string) => void) => {
          const file = blob instanceof File ? blob : new File([blob], 'pasted-image.png', { type: blob.type });
          try {
            const url = await uploadRef.current(file);
            callback(url, '이미지');
          } catch {
            /* 실패 안내는 업로드 함수가 한다 */
          }
        },
      },
    });
    instance.setHTML(initialHtml || '', false);
    editor.current = instance;
    return () => {
      instance.destroy();
      editor.current = null;
    };
    // initialHtml 은 처음 한 번만 쓴다(이후 내용은 에디터가 들고 있다)
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useImperativeHandle(ref, () => ({
    getHtml: () => editor.current?.getHTML() ?? '',
    setHtml: (html: string) => editor.current?.setHTML(html, false),
  }));

  return <div ref={el} className="rich-editor" />;
});

export default RichEditor;
