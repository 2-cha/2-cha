import { useAuth } from '@/hooks';
import {
  useCollectionQuery,
  useMemberCollectionsQuery,
  useMemberQuery,
} from '@/hooks/query';
import {
  ProfileCollection,
  ProfileHeader,
  ProfileReviewTab,
} from '@/components/Profile';

export default function ProfilePage() {
  const { user } = useAuth();
  const memberId = user?.sub;
  const { data: member } = useMemberQuery(memberId);
  const {
    data: collections,
    isLoading,
    isError,
  } = useMemberCollectionsQuery(member?.id);

  return member ? (
    <>
      <ProfileHeader member={member} isMe />
      {isLoading || isError ? null : (
        <ProfileCollection collections={collections} />
      )}
      <ProfileReviewTab memberId={member.id} />
    </>
  ) : (
    <div>member not found</div>
  );
}
