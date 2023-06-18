import { Fragment } from 'react';
import Link from 'next/link';
import Image from 'next/image';

import { PlaceSearchResult } from '@/types';
import { ShortTags } from '../Tags';
import BookmarkToggleButton from './BookmarkToggleButton';

import SadIcon from '../Icons/SadIcon';
import s from './PlaceList.module.scss';

interface PlaceListProps {
  pages: PlaceSearchResult[][];
}

export default function PlaceList({ pages }: PlaceListProps) {
  return (
    <ul className={s.container}>
      {pages.map((page, idx) => (
        <Fragment key={idx}>
          {page.map((place) => (
            <PlaceItem place={place} key={place.id} />
          ))}
        </Fragment>
      ))}
    </ul>
  );
}

interface PlaceItemProps {
  place: PlaceSearchResult;
}

export function PlaceItem({ place }: PlaceItemProps) {
  console.log(place);
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
        <Link href={`/places/${place.id}`}>
          <div className={s.placeItem__header}>
            <div className={s.placeItem__headerTop}>
              <p className={s.placeItem__title}>{place.name}</p>
            </div>
            <p className={s.placeItem__address}>
              {place.address} / {place.distance}
            </p>
          </div>
        </Link>
      </div>
      <BookmarkToggleButton
        size={48}
        className={s.placeItem__bookmark}
        itemType="places"
        itemId={place.id}
        isBookmarked={place.bookmark_status.is_bookmarked}
      />
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
        <div className={s.thumbnail__skeleton}>
          <SadIcon width={100} height={100} />
          <span className={s.thumbnail__skeleton__title}>
            사진을 찾을 수 없어요
          </span>
          <span className={s.thumbnail__skeleton__subtitle}>
            첫 리뷰어가 되어보세요
          </span>
        </div>
      )}
    </li>
  );
}
