import { fetchClient } from '@/lib/fetchClient';
import { useMutation } from '@tanstack/react-query';

interface Props {
  type: 'collections' | 'reviews';
  id: string | number;
}

async function requestDelete({ type, id }: Props) {
  const { data } = await fetchClient.delete(`/${type}/${id}`);

  return data;
}

export function useDeleteMutation() {
  const mutation = useMutation({
    mutationFn: (props: Props) => requestDelete(props),
    // onSuccess
    // onError
  });

  return mutation;
}
