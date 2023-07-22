import { useQuery } from '@tanstack/react-query';
import { fetchClient } from '@/lib/fetchClient';
import type { Tag } from '@/types';

async function fetchTags(query: string) {
  const { data } = await fetchClient.get<Tag[]>('/tags', {
    params: {
      query,
    },
  });

  return data;
}

export function useTagsQuery(query: string) {
  const result = useQuery({
    queryKey: ['tags', query],
    queryFn: () => fetchTags(query),
    refetchOnWindowFocus: false,
    staleTime: 1000 * 60 * 60 * 24 * 7, // 7 days
  });

  return result;
}
