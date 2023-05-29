import { fetchClient } from '@/lib/fetchClient';
import { QueryResponse } from '@/types';
import { useMutation } from '@tanstack/react-query';

interface PostImageResponse {
  url: string;
}

async function postImage(image: File) {
  const formData = new FormData();
  formData.append('file', image);

  const { data } = await fetchClient.post<QueryResponse<PostImageResponse>>(
    '/members/image',
    formData
  );

  if (!data.success) {
    throw new Error(data.message);
  }

  return data.data.url;
}

export function useProfileImageMutation() {
  const mutation = useMutation({
    mutationFn: async (image: File) => postImage(image),
  });

  return mutation;
}

// TODO: useReviewImage 와 합치기 가능해보임
