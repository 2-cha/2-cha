import { useInfiniteQuery } from '@tanstack/react-query';
import { fetchClient } from '@/lib/fetchClient';
import type { Coordinate } from '@/atoms/location';
import type { PlaceSearchResult } from '@/types';
import { useCurrentLocation } from '@/hooks/useCurrentLocation';

async function fetchPlaces({
  pageParam,
  location,
}: {
  pageParam: number;
  location: Coordinate | null;
}) {
  const params = {
    ...location,
    page_number: pageParam,
    max_dist: 2000,
  };
  const { data } = await fetchClient.get<PlaceSearchResult[]>(
    '/places/nearby',
    { params }
  );

  return data;
}

export function usePlacesQuery() {
  const { location } = useCurrentLocation();
  const result = useInfiniteQuery({
    queryKey: ['places', 'nearby', location],
    queryFn: ({ pageParam = 0 }) => fetchPlaces({ pageParam, location }),
    getNextPageParam: (lastPage, pages) =>
      lastPage?.length ? pages.length : undefined,
    enabled: !!location,
    retry: false,
  });
  return result;
}
