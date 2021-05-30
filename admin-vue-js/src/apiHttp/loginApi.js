import request from '@/util/requestUtils.js';

export function loginByAccountApi(userName, password, verifyKey, verifyCode) {
    const data = {
        userName,
        password
    };
    return request({
        url: '/api/signIn',
        method: 'post',
        data
    });
}

export function logoutApi() {
    return request({
        url: '/api/logout',
        method: 'post'
    });
}

export function getUserProfileApi(token) {
    return request({
        url: '/api/user',
        method: 'get',
        params: { token }
    });
}
