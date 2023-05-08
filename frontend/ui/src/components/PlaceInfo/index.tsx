import { forwardRef, useState } from 'react';
import { Map, MapMarker } from 'react-kakao-maps-sdk';
import PlaceReviews from './PlaceReviewsMenu';
import PlaceDetail from './PlaceDetailMenu';
import type { Place } from '@/types';
import cn from 'classnames';
import s from './PlaceInfo.module.scss';

const menuItems = {
  review: '리뷰',
  map: '지도',
  info: '정보',
};

interface PlaceInfoProps {
  placeInfo: Place;
}

export default forwardRef<HTMLParagraphElement, PlaceInfoProps>(
  function PlaceInfo({ placeInfo }: PlaceInfoProps, ref) {
    const [currentMenu, setCurrentMenu] = useState(menuItems.review);

    return (
      <div className={s.root}>
        {placeInfo.thumbnail && (
          <img
            width="100%"
            src={placeInfo.thumbnail.split(',')[0]}
            alt={placeInfo.name}
            className={s.thumbnail}
          />
        )}

        <div className={s.summary}>
          <p className={s.summary__title} ref={ref}>
            {placeInfo.name}
          </p>
          <p className={s.summary__category}>{placeInfo.category}</p>
        </div>
        {placeInfo.tags.map((tag) => (
          <span key={tag.id}>{tag.message}</span>
        ))}

        {/* TODO: refactor */}
        <div className={s.menu}>
          {Object.entries(menuItems).map(([item, label]) => (
            <button
              key={item}
              className={cn(s.menu__item, {
                [s.menu__itemActive]: label === currentMenu,
              })}
              onClick={() => setCurrentMenu(label)}
            >
              {label}
            </button>
          ))}
        </div>

        {currentMenu === menuItems.review ? (
          <PlaceReviews placeId={placeInfo.id} />
        ) : currentMenu === menuItems.map ? (
          <PlaceMap position={{ lat: placeInfo.lat, lng: placeInfo.lon }} />
        ) : currentMenu === menuItems.info ? (
          <PlaceDetail placeInfo={placeInfo} />
        ) : null}
      </div>
    );
  }
);

function PlaceMap({ position }: { position: { lat: number; lng: number } }) {
  return (
    <Map center={position} level={4} className={s.map}>
      <MapMarker position={position} />
    </Map>
  );
}
