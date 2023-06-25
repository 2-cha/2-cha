import { useQuery } from '@tanstack/react-query';

import { fetchClient } from '@/lib/fetchClient';

async function fetchCollections() {
  const { data } = await fetchClient.get<any>('/collections');

  return data;
}

export function useCollectionsQuery() {
  const result = useQuery({
    queryKey: ['collections'],
    queryFn: () => fetchCollections(),
    retry: false,
  });

  return result;
}
