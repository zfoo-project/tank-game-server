import { useCookies } from '@vueuse/integrations/useCookies'

const cookies = useCookies();

export const TokenKey = "tank";
export const BASE_URL = import.meta.env.VITE_API_BASE_URL;

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
