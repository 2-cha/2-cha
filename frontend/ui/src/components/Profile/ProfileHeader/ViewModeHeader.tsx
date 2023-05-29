import { Dispatch, SetStateAction, useCallback, useState } from 'react';
import Image from 'next/image';

import styles from './ViewModeHeader.module.scss';
import { Member } from '@/types';

interface Props {
  member: Member;
  isMe?: boolean;
  setIsEditing: Dispatch<SetStateAction<boolean>>;
}

export default function ViewModeHeader({ member, isMe, setIsEditing }: Props) {
  const handleClickEditButton = useCallback(
    function () {
      setIsEditing(true);
    },
    [setIsEditing]
  );

  return (
    <div className={styles.topDiv}>
      <div className={styles.imageWrapper}>
        <Image
          src={member.prof_img}
          width={120}
          height={120}
          alt="member profile pic"
          className={styles.image}
        />
      </div>
      <div className={styles.profileData}>
        <h1>{member.name}</h1>
        <h2>{member.prof_msg}</h2>
        {isMe ? (
          <button type="button" onClick={handleClickEditButton}>
            <span>프로필 편집</span>
          </button>
        ) : (
          <button type="button">
            <span>팔로우</span>
          </button>
        )}
      </div>
    </div>
  );
}
