import { useMutation } from '@tanstack/react-query';

import { fetchClient } from '@/lib/fetchClient';

interface RequestBookmarkProps {
  method: 'post' | 'delete';
  type: string;
  id: string | number;
}

async function requestBookmark({ method, type, id }: RequestBookmarkProps) {
  const { data } = await fetchClient[method](`/bookmarks/${type}/${id}`);

  return data;
}

export function useBookmarkMutation() {
  const mutation = useMutation({
    mutationFn: (props: RequestBookmarkProps) => requestBookmark(props),
    // onSuccess
    // onError
  });

  return mutation;
}
