import { forwardRef } from 'react';
import { Place } from '@/types';
import s from './PlaceInfo.module.scss';

interface PlaceInfoProps {
  placeInfo: Place;
}

export default forwardRef<HTMLParagraphElement, PlaceInfoProps>(
  function PlaceInfo({ placeInfo }: PlaceInfoProps, ref) {
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

        <p className={s.title} ref={ref}>
          {placeInfo.name}
        </p>
        <p>{placeInfo.address}</p>

        {placeInfo.tags.map((tag) => (
          <span key={tag.id}>{tag.message}</span>
        ))}
      </div>
    );
  }
);
