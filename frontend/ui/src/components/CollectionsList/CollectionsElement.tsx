import Image from 'next/image';
import Link from 'next/link';

import { Collection } from '@/types/collection';

import s from './CollectionsList.module.scss';

interface CollectionProps {
  collection: Collection;
}

export default function CollectionsElement({ collection }: CollectionProps) {
  return (
    <li className={s.element}>
      <img src={collection.thumbnail} alt={`${collection.title} thumbnail`} />
      <Link href={`/collections/${collection.id}`}>{collection.title}</Link>
    </li>
  );
}
