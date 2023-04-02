import { useCookies } from '@vueuse/integrations/useCookies'

const cookies = useCookies();

export const TokenKey = "tank";

export const BASE_URL = "http://tank-api.zfoo.net";
// export const BASE_URL = "http://localhost:9600";

export function getToken() {
    return cookies.get(TokenKey);
}

export function setToken(token) {
    return cookies.set(TokenKey, token);
}

export function removeToken() {
    return cookies.remove(TokenKey);
}

export function httpHeaders() {
  return {
    headers: {
      "tank": getToken(),
    },
  };
}
