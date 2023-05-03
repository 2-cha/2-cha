import { selector } from 'recoil';
import { tokenState } from './token';
import jwtDecode from 'jwt-decode';
import type { JwtPayload } from '@/types';

export const jwtPayloadState = selector<JwtPayload | null>({
  key: 'jwtPayloadState',
  get: ({ get }) => {
    const token = get(tokenState);
    if (token) {
      return jwtDecode<JwtPayload>(token.access_token);
    }
    return null;
  },
});
