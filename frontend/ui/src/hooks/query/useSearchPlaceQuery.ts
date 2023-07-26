import { useInfiniteQuery } from '@tanstack/react-query';
import { fetchClient } from '@/lib/fetchClient';
import type { Place } from '@/types';

async function searchPlace({
  query,
  pageParam,
}: {
  query: string;
  pageParam: number;
}) {
  const { data } = await fetchClient.get<Place[]>('/places', {
    params: {
      query,
      page_number: pageParam,
      page_size: 10,
    },
  });

  return data;
}

export function useSearchPlaceQuery(query: string) {
  const result = useInfiniteQuery({
    queryKey: ['places', 'search', query],
    queryFn: ({ pageParam = 0 }) => searchPlace({ query, pageParam }),
    getNextPageParam: (lastPage, pages) =>
      lastPage.length ? pages.length : undefined,
    enabled: query !== '',
    staleTime: 1000 * 60 * 5,
    refetchOnWindowFocus: false,
  });

  return result;
}
