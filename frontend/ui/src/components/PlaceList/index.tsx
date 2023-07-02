import { Fragment, useCallback, useState } from 'react';
import Link from 'next/link';
import Image from 'next/image';

import { PlaceSearchResult } from '@/types';
import { NewShortTags } from '../Tags';
import { BookmarkToggleButton } from '../Buttons';

import {
  CocktailIcon,
  PinIcon,
  WhiskeyIcon,
  WineIcon,
} from '@/components/Icons';
import NoImage from '../NoImage';

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
  const [isTooltipShown, setIsTooltipShown] = useState(false);

  const width = 48;
  const icon = {
    COCKTAIL_BAR: <CocktailIcon width={width} />,
    WINE_BAR: <WineIcon width={width} />,
    WHISKEY_BAR: <WhiskeyIcon width={width} />,
    BEER_BAR: '🍺',
  }[place.category];

  const handleOnHover = useCallback(function () {
    return () => setIsTooltipShown(true);
  }, []);

  const handleOnBlur = useCallback(function () {
    return () => setIsTooltipShown(false);
  }, []);

  return (
    <li className={s.placeItem}>
      <div className={s.placeItem__inner}>
        {place.tag_summary && (
          <NewShortTags
            className={s.placeItem__tags}
            tagList={place.tag_summary}
            keyID={place.id.toString()}
          />
        )}
        <Link href={`/places/${place.id}`}>
          <div className={s.placeItem__header}>
            <div className={s.placeItem__headerLeft}>
              <div>{icon}</div>
              {/* TODO: 아이콘 수정 */}
              <p className={s.placeItem__title}>{place.name}</p>
            </div>
            <div
              className={s.placeItem__headerRight}
              onFocus={handleOnHover()}
              onMouseOver={handleOnHover()}
              onBlur={handleOnBlur()}
              onMouseOut={handleOnBlur()}
            >
              <PinIcon />
              <span className={s.placeItem__distance}>
                {place.distance.toFixed(0)}m
              </span>
              {isTooltipShown && (
                <div className={s.placeItem__tooltip}>
                  <span>{place.address}</span>
                </div>
              )}
            </div>
          </div>
        </Link>
      </div>
      <BookmarkToggleButton
        size={48}
        className={s.placeItem__bookmark}
        itemType="places"
        itemId={place.id}
        isBookmarked={place.bookmark_status.is_bookmarked}
        bookmarkCount={place.bookmark_status.count}
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
        <NoImage />
      )}
    </li>
  );
}
