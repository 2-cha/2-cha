import { fetchClient } from '@/lib/fetchClient';
import type { Tag } from '@/types';
import { useMutation } from '@tanstack/react-query';

interface ReviewMutationProps {
  placeId?: string | string[];
  urls: string[];
  tags: Tag[];
}

async function postReview({ placeId, urls, tags }: ReviewMutationProps) {
  return fetchClient.post(`/places/${placeId}/reviews`, {
    tag_ids: tags.map((tag) => tag.id),
    img_urls: urls,
  });
}

export function useReviewMutation() {
  const mutation = useMutation({
    mutationKey: ['places', 'reviews'],
    mutationFn: (props: ReviewMutationProps) => postReview(props),
  });

  return mutation;
}
