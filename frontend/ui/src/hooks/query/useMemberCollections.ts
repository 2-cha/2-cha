import { useQuery } from '@tanstack/react-query';

import { fetchClient } from '@/lib/fetchClient';
import { Collection } from '@/types/collection';

type CollectionType = Omit<
  Collection,
  'member' | 'bookmark_status' | 'like_status'
>;

async function fetchMemberCollections({ memberId }: { memberId: number }) {
  const { data } = await fetchClient.get<CollectionType[]>(
    `/members/${memberId}/collections`
  );

  return data;
}

export function useMemberCollectionsQuery(memberId: number) {
  const result = useQuery({
    queryKey: ['members', memberId, 'collections'],
    queryFn: () => fetchMemberCollections({ memberId }),
    enabled: memberId != null,
  });

  return result;
}
