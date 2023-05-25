import { useAuth } from '@/hooks/useAuth';
import { useMemberQuery } from '@/hooks/query/useMember';
import Profile from '@/components/Profile';

export default function ProfilePage() {
  const { user } = useAuth();
  const memberId = user?.sub;
  const { data: member } = useMemberQuery(memberId);

  return member ? <Profile member={member} /> : <div>member not found</div>;
}
