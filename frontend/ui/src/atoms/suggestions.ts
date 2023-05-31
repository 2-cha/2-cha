import { atom } from 'recoil';
import type { SuggestionPlace } from '@/types';

export const suggestionsState = atom<SuggestionPlace[]>({
  key: 'suggestionsState',
  default: [],
});
