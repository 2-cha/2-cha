import Image from 'next/image';
import Link from 'next/link';

import { Collection } from '@/types/collection';

import s from './CollectionsElement.module.scss';
import { BookmarkIcon, HeartIcon } from '../Icons';
import { CSSProperties } from 'react';
import classNames from 'classnames';

interface CollectionProps {
  collection: Collection;
  style?: CSSProperties;
  className?: string;
}

export default function CollectionsElement({
  collection,
  style,
  className,
}: CollectionProps) {
  const { like_status, bookmark_status } = collection;

  return (
    <div className={classNames(className, s.element)}>
      <Image
        src={collection.thumbnail}
        width={480}
        height={480}
        unoptimized
        alt={`${collection.title} thumbnail`}
      />
      <Link href={`/collections/${collection.id}`} className={s.element__cover}>
        <div className={s.element__metadata}>
          <h2>{collection.title}</h2>
          <div className={s.element__icons}>
            {like_status != null && (
              <>
                <HeartIcon isFilled />
                <span>{like_status.count}</span>
              </>
            )}
            {bookmark_status != null && (
              <>
                <BookmarkIcon isSingle isActive />
                <span>{bookmark_status.count}</span>
              </>
            )}
          </div>
        </div>
      </Link>
    </div>
  );
}
