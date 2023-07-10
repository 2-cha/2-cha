import { Dispatch, SetStateAction, useCallback } from 'react';
import Image from 'next/image';

import { Member } from '@/types';
import { EditIcon, PlusSquareIcon } from '@/components/Icons';

import s from './ViewModeHeader.module.scss';
import { FollowToggleButton } from '@/components/Buttons';
import Link from 'next/link';
import { SettingsIcon } from '@/components/Icons';

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
    <div className={s.topDiv}>
      <Image
        src={member.prof_img}
        width={100}
        height={100}
        alt="member profile pic"
        className={s.image}
      />
      <div className={s.profileData}>
        <div className={s.profileData__under}>
          <h1>{member.name}</h1>
          <h2>{member.prof_msg}</h2>
        </div>
        {isMe ? (
          <div className={s.buttonWrapper}>
            <button type="button" onClick={handleClickEditButton}>
              <EditIcon />
            </button>
            <Link
              href="/profile/settings"
              className={s.buttonWrapper__settings}
            >
              <SettingsIcon />
            </Link>
          </div>
        ) : (
          <FollowToggleButton userId={member.id} />
        )}
      </div>
    </div>
  );
}
