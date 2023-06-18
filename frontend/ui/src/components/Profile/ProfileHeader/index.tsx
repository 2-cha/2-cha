import { useCallback, useState } from 'react';

import styles from './ProfileHeader.module.scss';
import EditModeHeader from './EditModeHeader';
import ViewModeHeader from './ViewModeHeader';
import { Member } from '@/types';

interface Props {
  member: Member;
  isMe?: boolean;
}

export default function ProfileHeader({ member, isMe }: Props) {
  const [isEditing, setIsEditing] = useState(false);

  return (
    <header className={styles.root}>
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
