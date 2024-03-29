import { fetchClient } from '@/lib/fetchClient';
import type { Review } from '@/types';
import { useInfiniteQuery } from '@tanstack/react-query';

async function fetchPlaceReviews({
  placeId,
  pageParam,
}: {
  placeId: number;
  pageParam: number;
}) {
  const { data } = await fetchClient.get<Review[]>(
    `/places/${placeId}/reviews`,
    { params: { page_number: pageParam } }
  );

  return data;
}

export function usePlaceReviewsQuery(placeId: number) {
  const result = useInfiniteQuery({
    queryKey: ['places', placeId, 'reviews'],
    queryFn: ({ pageParam = 0 }) => fetchPlaceReviews({ placeId, pageParam }),
    getNextPageParam: (lastPage, pages) =>
      lastPage.length ? pages.length : undefined,
    enabled: placeId != null,
  });

  return result;
}
