import { Place } from '@/types';
import s from './PlaceInfo.module.scss';

interface PlaceInfoProps {
  placeInfo: Place;
}

export default function PlaceInfo({ placeInfo }: PlaceInfoProps) {
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

      <p className={s.title}>{placeInfo.name}</p>
      <p>{placeInfo.address}</p>

      {placeInfo.tags.map((tag) => (
        <span key={tag.id}>{tag.message}</span>
      ))}
    </div>
  );
}
