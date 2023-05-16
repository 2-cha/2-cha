import { useQuery } from '@tanstack/react-query';
import { fetchClient } from '@/lib/fetchClient';
import type { QueryResponse, Tag } from '@/types';

async function fetchTags(query: string) {
  const { data } = await fetchClient.get<QueryResponse<Tag[]>>('/tags', {
    params: {
      query,
    },
  });

  if (!data.success) {
    throw new Error(data.message);
  }
  return data.data;
}

export function useTagsQuery(query: string) {
  const result = useQuery({
    queryKey: ['tags', query],
    queryFn: () => fetchTags(query),
    retry: false,
    refetchOnWindowFocus: false,
    enabled: !!query,
  });

  return result;
}
