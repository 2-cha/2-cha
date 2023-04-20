import { forwardRef, useState } from 'react';
import { Map, MapMarker } from 'react-kakao-maps-sdk';
import { usePlaceReviewsQuery } from '@/hooks/reviews';
import { Place } from '@/types';
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
          <PlaceMap placeInfo={placeInfo} />
        ) : currentMenu === menuItems.info ? (
          <PlaceDetail />
        ) : null}
      </div>
    );
  }
);

function PlaceReviews({ placeId }: { placeId: number }) {
  const { data: reviews, isLoading, isError } = usePlaceReviewsQuery(placeId);

  // TODO: 레이아웃 디자인, 무한스크롤
  return (
    <div>
      <ul>
        {reviews?.map((review) => (
          <li key={review.id}>{JSON.stringify(review)}</li>
        ))}
      </ul>
    </div>
  );
}

function PlaceMap({ placeInfo }: { placeInfo: Place }) {
  // TODO: placeInfo에서 실제 좌표 받기
  const coord = {
    lat: 37.4879759679358,
    lng: 127.065527640082,
  };

  return (
    <Map center={coord} level={4} className={s.map}>
      <MapMarker position={coord} />
    </Map>
  );
}

function PlaceDetail() {
  // TODO:
  // 1. 복사 가능한 주소
  // 2. 홈페이지 링크
  // 3. 기타 코멘트 혹은 소개글 같은 것도 여기에 속할듯
  return <div>상세 정보</div>;
}
