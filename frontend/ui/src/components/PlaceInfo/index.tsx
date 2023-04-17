import { forwardRef, useState } from 'react';
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
          <PlaceReviews />
        ) : currentMenu === menuItems.map ? (
          <PlaceMap />
        ) : currentMenu === menuItems.info ? (
          <PlaceDetail />
        ) : null}
      </div>
    );
  }
);

function PlaceReviews() {
  // TODO: reviews api
  return <div>리뷰</div>;
}

function PlaceMap() {
  // TODO: import kakao map
  return <div>지도</div>;
}

function PlaceDetail() {
  // TODO:
  // 1. 복사 가능한 주소
  // 2. 홈페이지 링크
  // 3. 기타 코멘트 혹은 소개글 같은 것도 여기에 속할듯
  return <div>상세 정보</div>;
}
