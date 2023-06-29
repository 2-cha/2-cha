import axios from 'axios';
import { useInfiniteQuery } from '@tanstack/react-query';
import { type KakaoResponse } from '@/lib/kakao';
import { type Place } from '@/pages/api/keyword';

enum CategoryCode {
  CAFE = 'CE7',
  RESTAURANT = 'FD6',
}

async function fetchKeyword(query: string, page: number) {
  const { data } = await axios.get<KakaoResponse<Place>>('/api/keyword', {
    params: {
      query,
      category_group_code: [CategoryCode.CAFE, CategoryCode.RESTAURANT].join(
        ','
      ),
      page,
    },
  });

  return data;
}

export function useKeywordQuery(query: string) {
  const result = useInfiniteQuery({
    queryKey: ['kakao', 'keyword', query],
    queryFn: ({ pageParam = 1 }) => fetchKeyword(query, pageParam),
    getNextPageParam: (lastPage, pages) =>
      lastPage.meta.is_end ? undefined : pages.length + 1,
    staleTime: 1000 * 60 * 60 * 24,
    enabled: !!query,
  });

  return result;
}
