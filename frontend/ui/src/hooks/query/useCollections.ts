import { useQuery } from '@tanstack/react-query';

import { fetchClient } from '@/lib/fetchClient';
import { useCurrentLocation } from '../useCurrentLocation';
import { Coordinate } from '@/atoms';

interface Params {
  location: Coordinate | null;
  distance?: string;
}

async function fetchCollections({ location, distance }: Params) {
  const params = { lat: location?.lat, lon: location?.lon, distance };
  const { data } = await fetchClient.get<any>('/collections', { params });

  return data;
}

export function useCollectionsQuery(distance?: string) {
  const { location } = useCurrentLocation();
  const result = useQuery({
    queryKey: ['collections'],
    queryFn: () => fetchCollections({ location, distance }),
    enabled: !!location,
  });

  return result;
}
