import axios from 'axios';
import { getToken, refreshToken } from './auth';
import { getRecoil } from 'recoil-nexus';
import { jwtPayloadState } from '@/atoms/jwtPayload';
import type { QueryResponse } from '@/types';

export const fetchClient = axios.create({
  baseURL:
    process.env.NODE_ENV === 'production' &&
    process.env.NEXT_PUBLIC_DEPLOYMENT === 'preview'
      ? '/proxy/api'
      : process.env.NEXT_PUBLIC_BASE_API_URL,
});

// 요청 헤더에 토큰을 추가
fetchClient.interceptors.request.use((config) => {
  const token = getToken();

  if (token) {
    config.headers.Authorization = `Bearer ${token.access_token}`;
  }
  return config;
});

// 토큰 만료시 refresh
fetchClient.interceptors.request.use(
  async (config) => {
    const token = getToken();

    if (token) {
      const jwtPayload = getRecoil(jwtPayloadState);
      if (jwtPayload?.exp && jwtPayload.exp * 1000 < Date.now()) {
        const token = await refreshToken();
        if (token) {
          config.headers.Authorization = `Bearer ${token.access_token}`;
        }
      }
    }
    return config;
  },
  null,
  { runWhen: (config) => config.url !== '/auth/refresh' }
);

// 401 응답시 토큰 갱신 후 재요청
fetchClient.interceptors.response.use(
  (res) => res,
  async (error) => {
    const { config, response } = error;
    if (
      !config._retry &&
      response?.status === 401 &&
      config.url !== '/auth/refresh'
    ) {
      config._retry = true;

      const token = await refreshToken();
      if (token) {
        return fetchClient.request(config);
      }
    }
    throw error;
  }
);

fetchClient.interceptors.response.use((res) => {
  const data = res.data as QueryResponse<any>;

  if (!data.success) {
    throw new Error(data.message);
  }

  res.data = data.data;
  return res;
});
