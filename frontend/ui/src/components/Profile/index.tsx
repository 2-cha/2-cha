import { Member } from '@/types';
import ProfileHeader from './ProfileHeader';
import ProfileCollection from './ProfileCollection';

interface Props {
  member: Member;
}

export default function Profile({ member }: Props) {
  return (
    <>
      <ProfileHeader member={member} />
      <ProfileCollection />
    </>
  );
}
