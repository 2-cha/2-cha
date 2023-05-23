import { fetchClient } from '@/lib/fetchClient';
import { useMutation } from '@tanstack/react-query';
import type { QueryResponse } from '@/types';

interface PostImageResponse {
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

  return data.data.url;
}

export function useReviewImageMutation() {
  const mutation = useMutation({
    mutationKey: ['reviews', 'images'],
    mutationFn: async (image: File) => postImage(image),
  });

  return mutation;
}
