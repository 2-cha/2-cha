import { useInfiniteQuery } from '@tanstack/react-query';
import { fetchClient } from '@/lib/fetchClient';
import { Coordinate } from '@/atoms/location';
import { Place, QueryResponse } from '@/types';
import { useCurrentLocation } from './location';

async function fetchPlaces({
  min_dist,
  location,
}: {
  min_dist: number;
  location: Coordinate | null;
}) {
  const params = {
    ...location,
    min_dist: min_dist,
    max_dist: min_dist + 1000,
    page_size: 10,
  };
  const { data } = await fetchClient.get<QueryResponse<Place[]>>(
    '/places/nearby',
    { params }
  );
  if (!data.success) {
    throw new Error(data.message);
  }
  return data.data;
}

export function usePlacesQuery() {
  const { location } = useCurrentLocation();
  const result = useInfiniteQuery({
    queryKey: ['places', 'nearby', location],
    queryFn: ({ pageParam = 0 }) =>
      fetchPlaces({ min_dist: pageParam, location }),
    enabled: !!location,
    getNextPageParam: (lastPage) => lastPage.at(-1)?.distance,
    retry: false,
  });
  return result;
}
