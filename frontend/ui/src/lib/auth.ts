import crypto from 'crypto';
import { resetRecoil, setRecoil, getRecoil } from 'recoil-nexus';
import { tokenState, type Token } from '@/atoms/token';
import { fetchClient } from './fetchClient';

function generateRandomString() {
  return crypto.randomBytes(16).toString('hex');
}

const REDIRECT_URL = process.env.NEXT_PUBLIC_ORIGIN + '/login/callback';
const GOOGLE_AUTH_BASE_URL = 'https://accounts.google.com/o/oauth2/v2/auth';

export const GOOGLE_AUTH_URL =
  GOOGLE_AUTH_BASE_URL +
  '?' +
  new URLSearchParams({
    response_type: 'code',
    client_id: process.env.NEXT_PUBLIC_GOOGLE_CLIENT_ID!,
    redirect_uri: REDIRECT_URL,
    scope: ['openid', 'profile', 'email'].join(' '),
    nonce: generateRandomString(),
  }).toString();

export function getToken() {
  return getRecoil(tokenState);
}

export async function refreshToken() {
  const token = getToken();
  if (!token) {
    return;
  }

  try {
    const { data } = await fetchClient.post<Token>('/auth/refresh', {
      refresh_token: token.refresh_token,
    });

    setRecoil(tokenState, data);
    return token;
  } catch {}

  // 토큰 갱신 실패시 로그아웃
  resetRecoil(tokenState);
}
