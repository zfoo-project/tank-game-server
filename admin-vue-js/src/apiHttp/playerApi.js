import request from '@/util/requestUtils.js';

export function searchApi(query, page, itemsPerPage) {
    return request({
        url: '/api/player/search',
        method: 'get',
        params: {
            query,
            page,
            itemsPerPage
        }
    });
}
