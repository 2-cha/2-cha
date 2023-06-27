import axios from 'axios';
import { useQuery } from '@tanstack/react-query';
import { type Place } from '@/pages/api/keyword';

enum CategoryCode {
  CAFE = 'CE7',
  RESTAURANT = 'FD6',
}

async function fetchKeyword(query: string) {
  const { data } = await axios.get<Place[]>('/api/keyword', {
    params: {
      query,
      category_group_code: [CategoryCode.CAFE, CategoryCode.RESTAURANT].join(
        ','
      ),
    },
  });

  return data;
}

export function useKeywordQuery(query: string) {
  const result = useQuery({
    queryKey: ['kakao', 'keyword', query],
    queryFn: () => fetchKeyword(query),
    staleTime: 1000 * 60 * 60 * 24,
    enabled: !!query,
  });

  return result;
}
