import { useQuery } from '@tanstack/react-query';
import { fetchClient } from '@/lib/fetchClient';
import type { CategorizedTag } from '@/types';

async function fetchTags(query: string) {
  const { data } = await fetchClient.get<CategorizedTag>('/tags/categorized', {
    params: {
      query,
    },
  });

  return data;
}

export function useCategorizedTagsQuery(query: string) {
  const result = useQuery({
    queryKey: ['tags', 'categorized', query],
    queryFn: () => fetchTags(query),
    retry: false,
    refetchOnWindowFocus: false,
    staleTime: 1000 * 60 * 60 * 24 * 7, // 7 days
  });

  return result;
}
