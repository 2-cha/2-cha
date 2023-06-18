import { useAuth } from '@/hooks/useAuth';
import { useMemberQuery } from '@/hooks/query/useMember';
import {
  ProfileCollection,
  ProfileHeader,
  ProfileReviewTab,
} from '@/components/Profile';

export default function ProfilePage() {
  const { user } = useAuth();
  const memberId = user?.sub;
  const { data: member } = useMemberQuery(memberId);

  return member ? (
    <>
      <ProfileHeader member={member} isMe />
      <ProfileCollection />
      <ProfileReviewTab memberId={member.id} />
    </>
  ) : (
    <div>member not found</div>
  );
}
