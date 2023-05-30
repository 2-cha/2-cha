import { recoilPersist } from 'recoil-persist';

export const RECOIL_PERSIST_KEY = 'recoil-persist';

export const { persistAtom } = recoilPersist({
  key: RECOIL_PERSIST_KEY,
});
