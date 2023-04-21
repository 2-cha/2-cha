import axios from 'axios';
import { useQuery } from '@tanstack/react-query';
import { Coordinate } from '@/atoms/location';
import { Region } from '@/pages/api/region';

async function fetchRegion(location: Coordinate) {
  const { data } = await axios.get<Region>('/api/region', {
    params: { ...location },
  });

  return data;
}

export function useRegionQuery(location: Coordinate | null) {
  const result = useQuery({
    queryKey: ['region', location],
    queryFn: () => fetchRegion(location!),
    enabled: !!location,
  });

  return result;
}
