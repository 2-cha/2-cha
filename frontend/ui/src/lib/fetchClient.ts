import axios from 'axios';

export const fetchClient = axios.create({
  baseURL:
    process.env.NODE_ENV === 'production' &&
    process.env.NEXT_PUBLIC_DEPLOYMENT === 'preview'
      ? '/proxy/api'
      : process.env.NEXT_PUBLIC_BASE_API_URL,
});

fetchClient.interceptors.request.use((config) => {
  // TODO: 실제 토큰으로 교체
  const token = process.env.NEXT_PUBLIC_TEMP_AUTH_TOKEN;
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
