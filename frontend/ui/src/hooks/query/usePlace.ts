import { fetchClient } from '@/lib/fetchClient';
import type { Place } from '@/types';
import { useQuery } from '@tanstack/react-query';

async function fetchPlace(placeId: string) {
  const { data } = await fetchClient.get<Place>(`/places/${placeId}`);

  return data;
}

export function usePlaceQuery(placeId?: string | string[]) {
  const result = useQuery({
    queryKey: ['places', placeId],
    queryFn: () => fetchPlace(placeId as string),
    refetchOnWindowFocus: false,
    enabled: !!placeId,
    retry: false,
  });

  return result;
}
