import { useRouter } from 'next/router';
import { useAuth, requireAuth } from '@/hooks';
import { useMemberCollectionsQuery, useMemberQuery } from '@/hooks/query';
import {
  ProfileCollection,
  ProfileHeader,
  ProfileReviewTab,
} from '@/components/Profile';
import MetaData from '@/components/MetaData';

export default function ProfilePage() {
  const { user } = useAuth();
  const { query } = useRouter();
  const { data: member } = useMemberQuery(query.memberId);
  const {
    data: collections,
    isLoading,
    isError,
  } = useMemberCollectionsQuery(member?.id);

  return (
    <>
      <MetaData title={`${member?.name}의 프로필`} />
      {member ? (
        <>
          <ProfileHeader member={member} isMe={user?.sub === query.memberId} />
          {isLoading || isError ? null : (
            <ProfileCollection
              collections={collections}
              isMe={user?.sub === query.memberId}
            />
          )}
          <ProfileReviewTab memberId={member.id} />
        </>
      ) : null}
    </>
  );
}
