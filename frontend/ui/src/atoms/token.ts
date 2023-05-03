import { atom } from 'recoil';
import { persistAtom } from './persist';

export interface Token {
  access_token: string;
  refresh_token?: string;
}

export const tokenState = atom<Token | null>({
  key: 'tokenState',
  default: null,
  effects: [persistAtom],
});
