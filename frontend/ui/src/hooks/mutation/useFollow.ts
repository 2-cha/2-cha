import { useMutation } from '@tanstack/react-query';

import { fetchClient } from '@/lib/fetchClient';

interface RequestFollowProps {
  method: 'post' | 'delete';
  id: string | number;
}

async function requestFollow({ method, id }: RequestFollowProps) {
  const { data } = await fetchClient[method](`/members/${id}/follow`);

  return data;
}

export function useFollowMutation() {
  const mutation = useMutation({
    mutationFn: (props: RequestFollowProps) => requestFollow(props),
  });

  return mutation;
}
