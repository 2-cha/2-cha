import { useInfiniteQuery } from '@tanstack/react-query';

import { fetchClient } from '@/lib/fetchClient';
import type { Coordinate, PlacesQueryParams } from '@/atoms';
import type { PlaceSearchResult } from '@/types';
import { useCurrentLocation } from '@/hooks';

async function fetchPlaces({
  pageParam,
  location,
  sort_by,
  sort_order,
  filter_by,
  filter_values,
}: PlacesQueryParams & {
  pageParam: number;
  location: Coordinate | null;
}) {
  const params = {
    ...location,
    page_number: pageParam,
    max_dist: 2000,
    sort_by,
    sort_order,
    filter_by,
    filter_values: filter_values?.join(','),
  };
  const { data } = await fetchClient.get<PlaceSearchResult[]>(
    '/places/nearby',
    { params }
  );

  return data;
}

export function usePlacesQuery(params: PlacesQueryParams = {}) {
  const { location } = useCurrentLocation();
  const result = useInfiniteQuery({
    queryKey: ['places', 'nearby', location, params],
    queryFn: ({ pageParam = 0 }) =>
      fetchPlaces({ pageParam, location, ...params }),
    getNextPageParam: (lastPage, pages) =>
      lastPage?.length ? pages.length : undefined,
    enabled: !!location,
    retry: false,
  });
  return result;
}
