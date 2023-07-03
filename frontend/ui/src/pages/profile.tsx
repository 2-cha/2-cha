import { useAuth, requireAuth } from '@/hooks';
import { useMemberCollectionsQuery, useMemberQuery } from '@/hooks/query';
import {
  ProfileCollection,
  ProfileHeader,
  ProfileReviewTab,
} from '@/components/Profile';
import MetaData from '@/components/MetaData';

export default requireAuth(function ProfilePage() {
  const { user } = useAuth();
  const memberId = user?.sub;
  const { data: member } = useMemberQuery(memberId);
  const {
    data: collections,
    isLoading,
    isError,
  } = useMemberCollectionsQuery(member?.id);

  return (
    <>
      <MetaData title="내 프로필" />
      {member ? (
        <>
          <ProfileHeader member={member} isMe />
          {isLoading || isError ? null : (
            <ProfileCollection collections={collections} />
          )}
          <ProfileReviewTab memberId={member.id} />
        </>
      ) : null}
    </>
  );
});
