import { fetchClient } from '@/lib/fetchClient';
import { useQuery } from '@tanstack/react-query';
import type { Member } from '@/types';

async function fetchMember(memberId: string) {
  const { data } = await fetchClient.get<Member>(`/members/${memberId}`);

  return data;
}

export function useMemberQuery(memberId?: string | string[]) {
  const result = useQuery({
    queryKey: ['members', memberId],
    queryFn: () => fetchMember(memberId as string),
    enabled: memberId != null,
  });

  return result;
}
