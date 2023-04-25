import { selector } from 'recoil';
import { accessTokenState } from './accessToken';
import jwtDecode from 'jwt-decode';
import type { JwtPayload } from '@/types';

export const jwtPayloadState = selector<JwtPayload | null>({
  key: 'jwtPayloadState',
  get: ({ get }) => {
    const token = get(accessTokenState);
    if (token) {
      return jwtDecode<JwtPayload>(token);
    }
    return null;
  },
});
