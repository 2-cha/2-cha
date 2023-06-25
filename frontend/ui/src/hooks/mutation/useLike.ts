import { fetchClient } from '@/lib/fetchClient';
import { useMutation } from '@tanstack/react-query';

interface Props {
  method: 'post' | 'delete';
  type: string;
  id: string | number;
}

async function requestLike({ method, type, id }: Props) {
  const { data } = await fetchClient[method](`${type}/${id}/like`);

  return data;
}

export function useLikeMutation() {
  const mutation = useMutation({
    mutationFn: (props: Props) => requestLike(props),
  });

  return mutation;
}
