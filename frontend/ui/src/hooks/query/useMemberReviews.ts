import { fetchClient } from '@/lib/fetchClient';
import { Review } from '@/types';
import { useInfiniteQuery } from '@tanstack/react-query';

async function fetchMemberReviews({
  memberId,
  pageParam,
}: {
  memberId: number;
  pageParam: number;
}) {
  const { data } = await fetchClient.get<Review[]>(
    `/members/${memberId}/reviews`,
    { params: { page_number: pageParam } }
  );
  return data;
}

export function useMemberReviewsQuery(memberId: number) {
  const result = useInfiniteQuery({
    queryKey: ['members', memberId, 'reviews'],
    queryFn: ({ pageParam = 0 }) => fetchMemberReviews({ memberId, pageParam }),
    getNextPageParam: (lastPage, pages) =>
      lastPage.length ? pages.length : undefined,
    enabled: memberId != null,
  });

  return result;
}
