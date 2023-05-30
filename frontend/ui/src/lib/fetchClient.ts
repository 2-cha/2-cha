import axios from 'axios';
import { RECOIL_PERSIST_KEY } from '@/atoms/persist';
import { tokenState, type Token } from '@/atoms/token';

function getToken(): Token | undefined {
  try {
    if (typeof window !== 'undefined') {
      const recoilStorage = window.localStorage.getItem(RECOIL_PERSIST_KEY);
      if (recoilStorage) {
        const recoilState = JSON.parse(recoilStorage);
        const token = recoilState[tokenState.key];
        return token;
      }
    }
  } catch {}

  return undefined;
}

export const fetchClient = axios.create({
  baseURL:
    process.env.NODE_ENV === 'production' &&
    process.env.NEXT_PUBLIC_DEPLOYMENT === 'preview'
      ? '/proxy/api'
      : process.env.NEXT_PUBLIC_BASE_API_URL,
});

fetchClient.interceptors.request.use((config) => {
  const token = getToken();

  if (token) {
    config.headers.Authorization = `Bearer ${token.access_token}`;
  }
  return config;
});
