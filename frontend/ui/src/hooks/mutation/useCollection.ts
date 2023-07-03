import { fetchClient } from '@/lib/fetchClient';
import { useMutation } from '@tanstack/react-query';

interface CollectionMutationProps {
  title: string;
  description: string;
  thumbnail: string;
  reviewIds: number[];
}

async function postCollection({
  title,
  description,
  thumbnail,
  reviewIds,
}: CollectionMutationProps) {
  return fetchClient.post(`/collections`, {
    title: title,
    description: description,
    thumbnail: thumbnail,
    review_ids: reviewIds,
  });
}

export function useCollectionMutation() {
  const mutation = useMutation({
    mutationKey: ['collections'],
    mutationFn: (props: CollectionMutationProps) => postCollection(props),
  });

  return mutation;
}
