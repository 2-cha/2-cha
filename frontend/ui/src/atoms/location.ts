import { atom } from 'recoil';

export interface Coordinate {
  lat: number;
  lon: number;
}

export const locationState = atom<Coordinate | null>({
  key: 'locationState',
  default: null,
  effects: [
    ({ onSet }) => {
      onSet(() => {
        if (window !== undefined) {
          window.scrollTo({ top: 0 });
        }
      });
    },
  ],
});
