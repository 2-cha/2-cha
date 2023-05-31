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

async function putImage(imageUrl: string) {
  return fetchClient.put('/members/image', {
    url: imageUrl,
  });
}

export function useProfileImageMutation() {
  const mutation = useMutation({
    mutationFn: async (image: File) => postImage(image),
  });

  return mutation;
}

export function useProfileImageUrlMutation() {
  const mutation = useMutation({
    mutationFn: async (imageUrl: string) => putImage(imageUrl),
  });

  return mutation;
}
