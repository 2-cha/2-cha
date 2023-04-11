import { Fragment } from 'react';
import Link from 'next/link';
import { Place } from '@/types';
import s from './PlaceList.module.scss';

interface PlaceListProps {
  pages: Place[][];
}

export default function PlaceList({ pages }: PlaceListProps) {
  return (
    <div className={s.container}>
      <ul className={s.placeList}>
        {pages.map((page, idx) => (
          <Fragment key={idx}>
            {page.map((place) => (
              <li key={place.id} className={s.placeList__item}>
                <PlaceItem place={place} />
              </li>
            ))}
          </Fragment>
        ))}
      </ul>
    </div>
  );
}

interface PlaceItemProps {
  place: Place;
}

export function PlaceItem({ place }: PlaceItemProps) {
  return (
    <div className={s.placeItem}>
      <div className={s.placeItem__header}>
        <Link href={`/places/${place.id}`}>
          <p className={s.placeItem__title}>{place.name}</p>
        </Link>
        <p className={s.placeItem__address}>
          {place.address} / {place.distance}
        </p>
      </div>
      {place.thumbnail ? (
        <div className={s.thumbnail__wrapper}>
          {/* TODO: fix to next/image */}
          <img
            src={place.thumbnail.split(',')[0]}
            width={480}
            loading="lazy"
            alt={place.name}
            className={s.thumbnail}
          />
        </div>
      ) : (
        <div className={s.thumbnail__skeleton} />
      )}
    </div>
  );
}
