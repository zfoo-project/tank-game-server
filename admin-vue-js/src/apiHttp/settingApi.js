import request from '@/util/requestUtils.js';


export function saveSettingApi(theme, language) {
    const data = {
        theme,
        language
    };
    return request({
        url: '/api/user/saveSetting',
        method: 'post',
        data
    });
}
