import { fetchClient } from '@/lib/fetchClient';
import { Collection } from '@/types/collection';
import { useQuery } from '@tanstack/react-query';

async function fetchCollection(collectionId: string) {
  const { data } = await fetchClient.get<Collection>(
    `/collections/${collectionId}`
  );

  return data;
}

export function useCollectionQuery(collectionId?: string | string[]) {
  const result = useQuery({
    queryKey: ['collections', collectionId],
    queryFn: () => fetchCollection(collectionId as string),
    refetchOnWindowFocus: false,
    enabled: !!collectionId,
  });

  return result;
}

async function fetchCollectionRecommendations(collectionId: string) {
  const { data } = await fetchClient.get<Collection[]>(
    `/collections/${collectionId}/recommendation`
  );

  return data;
}

export function useCollectionRecommendations(collectionId?: string | string[]) {
  const res = useQuery({
    queryKey: ['collections', collectionId, 'recommendation'],
    queryFn: () => fetchCollectionRecommendations(collectionId as string),
    refetchOnWindowFocus: false,
    enabled: !!collectionId,
  });

  return res;
}
