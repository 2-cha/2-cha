import { fetchClient } from '@/lib/fetchClient';
import { useMutation } from '@tanstack/react-query';
import type { Place, QueryResponse } from '@/types';

export interface PostImageResponse {
  suggestions: Pick<Place, 'id' | 'name' | 'address' | 'category'>[];
  url: string;
}

async function postImage(image: File) {
  const formData = new FormData();
  formData.append('file', image);

  const { data } = await fetchClient.post<QueryResponse<PostImageResponse>>(
    '/reviews/images',
    formData
  );
  if (!data.success) {
    throw new Error(data.message);
  }

  return data.data;
}

export function useReviewImageMutation() {
  const mutation = useMutation({
    mutationKey: ['reviews', 'images'],
    mutationFn: async (image: File) => postImage(image),
  });

  return mutation;
}
