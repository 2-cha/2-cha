import { fetchClient } from '@/lib/fetchClient';
import { Place, QueryResponse } from '@/types';
import { useQuery } from '@tanstack/react-query';

async function fetchPlace(placeId: string) {
  const { data } = await fetchClient<QueryResponse<Place>>(
    `/places/${placeId}`
  );
  if (!data.success) {
    throw new Error(data.message);
  }
  return data.data;
}

export function usePlaceQuery(placeId?: string | string[]) {
  const result = useQuery({
    queryKey: ['places', placeId],
    queryFn: () => fetchPlace(placeId as string),
    refetchOnWindowFocus: false,
    enabled: !!placeId,
  });

  return result;
}
