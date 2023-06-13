import { atom, selector } from 'recoil';

export interface SortBy {
  sort_by?: 'distance' | 'tag_count' | 'review_count';
  sort_order?: 'asc' | 'desc';
}

export interface FilterBy {
  filter_by?: 'category' | 'tag';
  filter_values?: string[];
}

export type PlacesQueryParams = SortBy & FilterBy;

export const placeSortByState = atom<SortBy>({
  key: 'placeSortBy',
  default: { sort_by: 'distance', sort_order: 'asc' },
});

export const placeFilterByState = atom<FilterBy | null>({
  key: 'placeFilterBy',
  default: null,
});

export const placesQueryParamsState = selector<PlacesQueryParams>({
  key: 'placesQueryParams',
  get: ({ get }) => {
    const sortBy = get(placeSortByState);
    const filterBy = get(placeFilterByState);

    return {
      ...sortBy,
      ...filterBy,
    };
  },
});
