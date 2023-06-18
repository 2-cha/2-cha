import { useState } from 'react';

import EditModeHeader from './EditModeHeader';
import ViewModeHeader from './ViewModeHeader';
import { Member } from '@/types';

import s from './ProfileHeader.module.scss';

interface Props {
  member: Member;
  isMe?: boolean;
}

export default function ProfileHeader({ member, isMe }: Props) {
  const [isEditing, setIsEditing] = useState(false);

  return (
    <header className={s.root}>
      {isEditing && isMe ? (
        <EditModeHeader member={member} setIsEditing={setIsEditing} />
      ) : (
        <ViewModeHeader
          member={member}
          setIsEditing={setIsEditing}
          isMe={isMe}
        />
      )}
    </header>
  );
}
