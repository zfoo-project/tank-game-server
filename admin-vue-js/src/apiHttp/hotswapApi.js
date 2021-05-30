import request from '@/util/requestUtils.js';

export function javaHotswapApi(formData) {
    return request({
        url: '/api/java/hotswap',
        method: 'post',
        data: formData
    });
}


export function excelHotswapApi(formData) {
    return request({
        url: '/api/excel/hotswap',
        method: 'post',
        data: formData
    });
}

