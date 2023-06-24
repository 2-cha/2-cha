import axios from 'axios';
import { useQuery } from '@tanstack/react-query';
import type { Coordinate } from '@/atoms';
import type { Region } from '@/pages/api/region';

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
    staleTime: 1000 * 60 * 60 * 24,
    enabled: !!location,
  });

  return result;
}

export function useRegion(location: Coordinate | null) {
  const { data } = useRegionQuery(location);

  return data ? `${data.region_2depth_name} ${data.region_3depth_name}` : null;
}
