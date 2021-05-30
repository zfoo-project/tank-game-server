import Clipboard from 'clipboard';
import i18n from '@/i18n/i18n.js';
import { simpleSuccess, simpleError } from '@/util/noticeUtils.js';

export default function handleClipboard(text, event) {
    const clipboard = new Clipboard(event.target, {
        text: () => _.toString(text)
    });
    clipboard.on('success', () => {
        simpleSuccess(i18n.t('notice.copySuccess'));
        clipboard.off('error');
        clipboard.off('success');
        clipboard.destroy();
    });
    clipboard.on('error', () => {
        simpleError(i18n.t('notice.copyError'));
        clipboard.off('error');
        clipboard.off('success');
        clipboard.destroy();
    });
    clipboard.onClick(event);
}
