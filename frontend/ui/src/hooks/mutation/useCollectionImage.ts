import { fetchClient } from '@/lib/fetchClient';
import { useMutation } from '@tanstack/react-query';

interface PostImageResponse {
  url: string;
}

async function postImage(image: File) {
  const formData = new FormData();
  formData.append('file', image);

  const { data } = await fetchClient.post<PostImageResponse>(
    '/collections/image',
    formData
  );

  return data.url;
}

export function useCollectionImageMutation() {
  const mutation = useMutation({
    mutationFn: async (image: File) => postImage(image),
  });
}
