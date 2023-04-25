import { fetchClient } from '@/lib/fetchClient';
import { useQuery } from '@tanstack/react-query';
import type { Member, QueryResponse } from '@/types';

async function fetchMember(memberId: number) {
  const { data } = await fetchClient.get<QueryResponse<Member>>(
    `/members/${memberId}`
  );

  if (!data.success) {
    throw new Error(data.message);
  }
  return data.data;
}

export function useMemberQuery(memberId?: number) {
  const result = useQuery({
    queryKey: ['members', memberId],
    queryFn: () => fetchMember(memberId!),
    enabled: memberId != null,
  });

  return result;
}
