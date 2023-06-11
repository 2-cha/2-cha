import { Fragment } from 'react';
import Link from 'next/link';
import Image from 'next/image';

import { PlaceSearchResult } from '@/types';
import { ShortTags } from '../Tags';
import BookmarkToggleButton from '@/components/BookmarkToggleButton';

import s from './PlaceList.module.scss';

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
      <div className={s.placeItem__inner}>
        {place.tag_summary && (
          <ShortTags
            className={s.placeItem__tags}
            tagList={place.tag_summary}
            keyID={place.id.toString()}
          />
        )}
        <div className={s.placeItem__header}>
          <div className={s.placeItem__headerTop}>
            <Link href={`/places/${place.id}`}>
              <p className={s.placeItem__title}>{place.name}</p>
            </Link>
            <BookmarkToggleButton
              itemType="places"
              itemId={place.id}
              isBookmarked={place.bookmark_status.is_bookmarked}
            />
          </div>
          <p className={s.placeItem__address}>
            {place.address} / {place.distance}
          </p>
        </div>
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
