import { Member } from '@/types';
import ProfileHeader from './ProfileHeader';

interface Props {
  member: Member;
}

export default function Profile({ member }: Props) {
  return (
    <>
      <ProfileHeader member={member} />
      <div>컬렉션</div>
    </>
  );
}
