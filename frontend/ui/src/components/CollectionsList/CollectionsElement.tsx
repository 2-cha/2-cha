import Image from 'next/image';
import Link from 'next/link';

import { Collection } from '@/types/collection';

import s from './CollectionsElement.module.scss';
import { BookmarkIcon, HeartIcon } from '../Icons';

interface CollectionProps {
  collection: Collection;
}

export default function CollectionsElement({ collection }: CollectionProps) {
  const { like_status, bookmark_status } = collection;

  return (
    <li className={s.element}>
      <img src={collection.thumbnail} alt={`${collection.title} thumbnail`} />
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
    </li>
  );
}
