import { Member } from '@/types';
import ProfileHeader from './ProfileHeader';
import ProfileCollection from './ProfileCollection';
import ProfileReviewTab from './ProfileReviewTab';

interface Props {
  member: Member;
}

export default function Profile({ member }: Props) {
  return (
    <>
      <ProfileHeader member={member} />
      <ProfileCollection />
      <ProfileReviewTab />
    </>
  );
}
