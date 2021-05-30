import axios from 'axios';
import i18n from '@/i18n/i18n.js';

import { getXToken, getTokenKey } from '@/util/authUtils.js';
import { simpleSuccess, simpleError } from '@/util/noticeUtils.js';
import { isBlank } from '@/util/stringUtils.js';

// create an axios instance
const service = axios.create({
    baseURL: process.env.VUE_APP_BASE_API, // apiHttp 的 base_url
    timeout: 5000 // request timeout
});


// request interceptor
service.interceptors.request.use(
    request => {
        // Do something before request is sent
        const token = getXToken();
        if (!isBlank(token)) {
            // 平时开发里，先请求一个 token ，然后后面的请求都要带上这个 token 来进行认证或者授权，是一个常见的需求
            // 让每个请求携带token-- ['X-Token']为自定义key 请根据实际情况自行修改
            request.headers[getTokenKey()] = token;
        }
        console.log('Http发送：', request);
        return request;
    },
    error => {
        // Do something with request error
        console.log(error); // for debug
        Promise.reject(error);
    }
);

// response interceptor
service.interceptors.response.use(
    response => {
        const data = response.data;
        console.log('Http收到：', data);
        // 如果返回的是错误码，则通过snackbar显示出来
        if (_.isEqual(data.code, 1) || _.isNil(data.code)) {
            simpleSuccess(i18n.t('code_enum_1'));
        } else if (_.isEqual(data.code, 2)) {
            // DO noting，请求成功，但是不会在客户端上显示提示
        } else {
            simpleError(i18n.t('code_enum_' + data.code), data.code);
            throw data;
        }

        return response.data;
    },
    error => {
        console.log('err' + error); // for debug
        return Promise.reject(error);
    }
);

export default service;
