import axios from 'axios';
import { useQuery } from '@tanstack/react-query';
import { type Address } from '@/pages/api/address';

async function fetchAddress(address: string) {
  const { data } = await axios.get<Address[]>('/api/address', {
    params: { query: address },
  });

  return data;
}

export function useAddressQuery(
  address: string,
  type?: Address['address_type']
) {
  const result = useQuery({
    queryKey: ['kakao', 'address', address],
    queryFn: () => fetchAddress(address),
    select: (addresses) => {
      if (type) {
        return addresses.filter((address) => address.address_type === type);
      } else {
        return addresses;
      }
    },
    staleTime: 1000 * 60 * 60 * 24,
    enabled: !!address,
  });

  return result;
}
