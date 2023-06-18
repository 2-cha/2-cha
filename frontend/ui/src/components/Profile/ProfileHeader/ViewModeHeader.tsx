import { Dispatch, SetStateAction, useCallback } from 'react';
import Image from 'next/image';

import { Member } from '@/types';

import EditIcon from '@/components/Icons/EditIcon';
import PlusSquareIcon from '@/components/Icons/PlusSquareIcon';

import styles from './ViewModeHeader.module.scss';

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
      <Image
        src={member.prof_img}
        width={100}
        height={100}
        alt="member profile pic"
        className={styles.image}
      />
      <div className={styles.profileData}>
        <div className={styles.profileData__under}>
          <h1>{member.name}</h1>
          <h2>aaa{member.prof_msg}</h2>
        </div>
        {isMe ? (
          <button type="button" onClick={handleClickEditButton}>
            <EditIcon />
          </button>
        ) : (
          <button type="button">
            <PlusSquareIcon />
          </button>
        )}
      </div>
    </div>
  );
}
