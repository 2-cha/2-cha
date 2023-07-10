import Image from 'next/image';
import Link from 'next/link';
import cn from 'classnames';

import s from './ProfileButton.module.scss';

interface Props {
  imageSrc: string;
  imageSize: number;
  imageAlt: string;
  memberName: string;
  memberId: string | number;
  className?: string;
}

export default function ProfileButton({
  imageSrc,
  imageSize,
  imageAlt,
  memberName,
  memberId,
  className,
}: Props) {
  return (
    <Link href={`/profile/${memberId}`} className={cn(className, s.wrapper)}>
      <Image
        src={imageSrc}
        width={imageSize}
        height={imageSize}
        alt={imageAlt}
      />
      <h3>{memberName}</h3>
    </Link>
  );
}
