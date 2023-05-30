import axios from 'axios';

export const fetchClient = axios.create({
  baseURL:
    process.env.NODE_ENV === 'production' &&
    process.env.NEXT_PUBLIC_DEPLOYMENT === 'preview'
      ? '/proxy/api'
      : process.env.NEXT_PUBLIC_BASE_API_URL,
});

fetchClient.interceptors.request.use((config) => {
  let token;

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
