import { atom } from 'recoil';

export interface Coordinate {
  lat: number;
  lon: number;
}

export const locationState = atom<Coordinate | null>({
  key: 'locationState',
  default: null,
});
