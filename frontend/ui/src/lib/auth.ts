import crypto from 'crypto';

function generateRandomString() {
  return crypto.randomBytes(16).toString('hex');
}

const GOOGLE_AUTH_BASE_URL = 'https://accounts.google.com/o/oauth2/v2/auth';

export const GOOGLE_AUTH_URL =
  GOOGLE_AUTH_BASE_URL +
  '?' +
  new URLSearchParams({
    response_type: 'code',
    client_id: process.env.NEXT_PUBLIC_GOOGLE_CLIENT_ID!,
    redirect_uri: 'http://localhost:3000' + '/login/callback',
    scope: ['openid', 'profile', 'email'].join(' '),
    nonce: generateRandomString(),
  }).toString();
