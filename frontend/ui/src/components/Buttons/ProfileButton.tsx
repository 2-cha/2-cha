import Image from 'next/image';
import Link from 'next/link';
import cn from 'classnames';

import type { Member } from '@/types';

import s from './ProfileButton.module.scss';
import NoImage from '../NoImage';

interface Props {
  member: Member | null;
  imageSize: number;
  className?: string;
}

export default function ProfileButton({ member, imageSize, className }: Props) {
  return (
    <>
      {member ? (
        <Link
          href={`/profile/${member.id}`}
          className={cn(className, s.wrapper)}
        >
          <Image
            src={member.prof_img}
            width={imageSize}
            height={imageSize}
            alt={member.name}
          />
          <h3>{member.name}</h3>
        </Link>
      ) : (
        <div className={cn(className, s.wrapper)}>
          <div className={s.noImage}>
            <NoImage iconSize={26} />
          </div>
          <h3>탈퇴한 회원</h3>
        </div>
      )}
    </>
  );
}
