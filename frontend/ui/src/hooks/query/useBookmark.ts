import { fetchClient } from '@/lib/fetchClient';
import { useQuery } from '@tanstack/react-query';

async function fetchBookmarks(type: string) {
  const { data } = await fetchClient.get(`/bookmarks/${type}`);
  return data;
}

export function useBookmarkQuery(type: string) {
  const result = useQuery({
    queryKey: ['bookmarks', type],
    queryFn: () => fetchBookmarks(type),
  });

  return result;
}
