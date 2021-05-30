import store from '@/store/store.js';

export function simpleSuccess(str) {
    const snackbar = store.state.app.snackbar;
    if (_.isNil(snackbar)) {
        return;
    }
    snackbar.showSuccessSnackbar(str);
}

export function simpleError(str, code) {
    const snackbar = store.state.app.snackbar;
    if (_.isNil(snackbar)) {
        return;
    }
    snackbar.showErrorSnackbar(str, code);
}
