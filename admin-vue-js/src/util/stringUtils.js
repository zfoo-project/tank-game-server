export function isBlank(str) {
    if (_.isNil(str)) {
        return true;
    }
    if (_.isEmpty(str.trim())) {
        return true;
    }
    return false;
}


export function numFormat(num) {
    if (num >= 10000) {
        return _.round(num / 10000, 2) + 'W';
    } else if (num >= 1000) {
        return _.round(num / 1000, 2) + 'K';
    }
    return num;
}

export function toTagOrName(userCache) {
    if (isBlank(userCache.tag)) {
        return userCache.name;
    }
    return userCache.tag;
}
