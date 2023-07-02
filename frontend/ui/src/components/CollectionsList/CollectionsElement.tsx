import Image from 'next/image';
import Link from 'next/link';

import { Collection } from '@/types/collection';

import s from './CollectionsElement.module.scss';
import { BookmarkIcon, HeartIcon } from '../Icons';
import { CSSProperties } from 'react';

interface CollectionProps {
  collection: Collection;
  style: CSSProperties;
}

export default function CollectionsElement({
  style,
  collection,
}: CollectionProps) {
  const { like_status, bookmark_status } = collection;

  return (
    <div className={s.element} style={style}>
      <Image
        src={collection.thumbnail}
        width={480}
        height={480}
        alt={`${collection.title} thumbnail`}
      />
      <Link href={`/collections/${collection.id}`} className={s.element__cover}>
        <div className={s.element__metadata}>
          <h2>{collection.title}</h2>
          <div className={s.element__icons}>
            <HeartIcon isFilled />
            <span>{like_status.count}</span>
            <BookmarkIcon isSingle isActive />
            <span>{bookmark_status.count}</span>
          </div>
        </div>
      </Link>
    </div>
  );
}
