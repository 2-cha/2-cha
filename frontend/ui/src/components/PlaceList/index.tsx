import { Fragment } from 'react';
import Link from 'next/link';
import Image from 'next/image';

import { PlaceSearchResult } from '@/types';
import s from './PlaceList.module.scss';
import BookmarkIcon from '../Icons/BookmarkIcon';

interface PlaceListProps {
  pages: PlaceSearchResult[][];
}

export default function PlaceList({ pages }: PlaceListProps) {
  return (
    <>
      <div className={s.container}>
        <ul className={s.placeList}>
          {pages.map((page, idx) => (
            <Fragment key={idx}>
              {page.map((place) => (
                <PlaceItem place={place} key={place.id} />
              ))}
            </Fragment>
          ))}
        </ul>
      </div>
    </>
  );
}

interface PlaceItemProps {
  place: PlaceSearchResult;
}

export function PlaceItem({ place }: PlaceItemProps) {
  return (
    <li className={s.placeItem}>
      <div className={s.placeItem__header}>
        <div className={s.placeItem__headerTop}>
          <Link href={`/places/${place.id}`}>
            <p className={s.placeItem__title}>{place.name}</p>
          </Link>
          <BookmarkIcon isSingle /> {/* TODO: is bookmarked */}
        </div>
        <p className={s.placeItem__address}>
          {place.address} / {place.distance}
        </p>
      </div>
      {place.image ? (
        <Image
          src={place.image}
          width={480}
          height={480}
          loading="lazy"
          alt={place.name}
          className={s.thumbnail}
        />
      ) : (
        <div className={s.thumbnail__skeleton} />
      )}
    </li>
  );
}
