import { fetchClient } from '@/lib/fetchClient';
import type { QueryResponse, Review } from '@/types';
import { useQuery } from '@tanstack/react-query';

async function fetchPlaceReviews(placeId: number) {
  const { data } = await fetchClient<QueryResponse<Review[]>>(
    `/places/${placeId}/reviews`
  );
  if (!data.success) {
    throw new Error(data.message);
  }
  return data.data;
}

export function usePlaceReviewsQuery(placeId: number) {
  const result = useQuery({
    queryKey: ['places', placeId, 'reviews'],
    queryFn: () => fetchPlaceReviews(placeId),
  });

  return result;
}
