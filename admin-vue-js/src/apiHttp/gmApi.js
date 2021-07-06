import request from '@/util/requestUtils.js';

export function playerLevelApi(userName, playerLevel, exp) {
    const data = {
        userName,
        playerLevel,
        exp
    };

    return request({
        url: '/api/gm/playerLevel',
        method: 'post',
        data
    });
}

export function currencyApi(userName, gold, gem, energy) {
    const data = {
        userName,
        gold,
        gem,
        energy
    };

    return request({
        url: '/api/gm/currency',
        method: 'post',
        data
    });
}
