import { forwardRef, useState } from 'react';
import Image from 'next/image';
import { Map, MapMarker } from 'react-kakao-maps-sdk';

import type { Place } from '@/types';
import { getCategoryLabel } from '@/lib/placeUtil';
import PlaceReviews from './PlaceReviewsMenu';
import PlaceDetail from './PlaceDetailMenu';
import { Tags } from '../Tags';
import Tab from '../Tab';

import s from './PlaceInfo.module.scss';

const menuItems = ['리뷰', '지도', '정보'];
const MENU_REVIEW = 0;
const MENU_MAP = 1;
const MENU_INFO = 2;

interface PlaceInfoProps {
  placeInfo: Place;
}

export default forwardRef<HTMLParagraphElement, PlaceInfoProps>(
  function PlaceInfo({ placeInfo }: PlaceInfoProps, ref) {
    const [currentMenu, setCurrentMenu] = useState(MENU_REVIEW);

    return (
      <div className={s.root}>
        {placeInfo.image ? (
          <Image
            width={480}
            height={200}
            unoptimized
            src={placeInfo.image}
            alt={placeInfo.name}
            className={s.thumbnail}
          />
        ) : (
          <div className={s.thumbnail} />
        )}

        <div className={s.wrapper}>
          <div className={s.summary}>
            <p className={s.summary__title} ref={ref}>
              {placeInfo.name}
            </p>
            <p className={s.summary__category}>
              {getCategoryLabel(placeInfo.category)}
            </p>
          </div>
          {placeInfo.tags && placeInfo.tags.length > 0 && (
            <Tags
              keyID={`place-${placeInfo.id}`}
              tagList={placeInfo.tags}
              limit={5}
              isNumberShown
            />
          )}
          <Tab
            menuList={menuItems}
            isSticky
            currentIndex={currentMenu}
            setCurrentIndex={setCurrentMenu}
          />
          {currentMenu === MENU_REVIEW ? (
            <PlaceReviews placeId={placeInfo.id} />
          ) : currentMenu === MENU_MAP ? (
            <PlaceMap position={{ lat: placeInfo.lat, lng: placeInfo.lon }} />
          ) : currentMenu === MENU_INFO ? (
            <PlaceDetail placeInfo={placeInfo} />
          ) : null}
        </div>
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
