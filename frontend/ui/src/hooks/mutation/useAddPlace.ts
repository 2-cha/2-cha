import { fetchClient } from '@/lib/fetchClient';
import { useMutation } from '@tanstack/react-query';

export interface postPlaceProps {
  name: string;
  address: string;
  lot_address?: string;
  category?: string;
  lat: string;
  lon: string;
}

async function postPlace(props: postPlaceProps) {
  const { data } = await fetchClient.post('/places', props);
  return data;
}

export function useAddPlaceMutation() {
  const mutate = useMutation({
    mutationFn: (props: postPlaceProps) => postPlace(props),
  });

  return mutate;
}
