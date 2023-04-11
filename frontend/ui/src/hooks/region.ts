import axios from 'axios';
import { useQuery } from '@tanstack/react-query';
import { useCurrentLocation } from './location';
import { Coordinate } from '@/atoms/location';
import { Region } from '@/pages/api/region';

async function fetchRegion(location: Coordinate) {
  const { data } = await axios.get<Region>('/api/region', {
    params: { ...location },
  });

  return data;
}

export function useRegion() {
  const { location } = useCurrentLocation();
  const result = useQuery({
    queryKey: ['region', location],
    queryFn: () => fetchRegion(location!),
    enabled: !!location,
  });

  return result;
}
