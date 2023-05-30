import { useMutation } from '@tanstack/react-query';
import { fetchClient } from '@/lib/fetchClient';
import type { QueryResponse } from '@/types';

interface RequestBookmarkProps {
  method: 'post' | 'delete';
  type: string;
  id: string | number;
}

async function requestBookmark({ method, type, id }: RequestBookmarkProps) {
  const { data } = await fetchClient[method]<QueryResponse<{}>>(
    `/bookmarks/${type}/${id}`
  );

  if (!data.success) {
    throw new Error(data.message);
  }

  return true;
}

export function useBookmakrMutation() {
  const mutation = useMutation({
    mutationFn: (props: RequestBookmarkProps) => requestBookmark(props),
    // onSuccess
    // onError
  });

  return mutation;
}
