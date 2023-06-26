import { fetchClient } from '@/lib/fetchClient';
import { useMutation } from '@tanstack/react-query';

async function postPlace(props: any) {
  const { data } = await fetchClient.post('/places', props);
  return data;
}

export function useRegistPlaceMutation() {
  const mutate = useMutation({
    mutationFn: (props: any) => postPlace(props),
  });

  return mutate;
}
