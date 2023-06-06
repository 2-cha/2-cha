import { fetchClient } from '@/lib/fetchClient';
import { useMutation } from '@tanstack/react-query';
import type { SuggestionPlace, QueryResponse } from '@/types';

export interface PostImageResponse {
  suggestions: SuggestionPlace[];
  url: string;
}

async function postImage(image: File) {
  const formData = new FormData();
  formData.append('file', image);

  const { data } = await fetchClient.post<QueryResponse<PostImageResponse>>(
    '/reviews/images',
    formData
  );

  return data;
}

export function useReviewImageMutation() {
  const mutation = useMutation({
    mutationKey: ['reviews', 'images'],
    mutationFn: async (image: File) => postImage(image),
  });

  return mutation;
}
