import { useAuth } from '@/hooks/useAuth';
import { useMemberQuery } from '@/hooks/query/useMember';

export default function Profile() {
  const { user } = useAuth();
  const memberId = user?.sub;
  const { data: member } = useMemberQuery(memberId);

  return <div>{member ? <p>hello {member.name}</p> : null}</div>;
}
