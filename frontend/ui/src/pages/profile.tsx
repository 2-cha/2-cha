import { useAuth } from '@/hooks/auth';
import { useMemberQuery } from '@/hooks/members';

export default function Profile() {
  const { user } = useAuth();
  const memberId = user?.sub;
  const { data: member } = useMemberQuery(memberId);

  return <div>{member ? <p>hello {member.name}</p> : null}</div>;
}
