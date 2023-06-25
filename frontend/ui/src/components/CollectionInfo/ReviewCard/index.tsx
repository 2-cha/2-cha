import Image from 'next/image';

import { Review } from '@/types';
import { Tag } from '@/components/Tags';

import s from './ReviewCard.module.scss';
import NoImage from '@/components/NoImage';

interface Props {
  review: Omit<Review, 'member'>;
}

export default function ReviewCard({ review }: Props) {
  const { place } = review;

  return (
    <div className={s.wrapper}>
      {review.images && review.images[0] ? (
        <Image
          src={review.images[0]}
          alt="collection image"
          width={480}
          height={480}
          className={s.wrapper__image}
        />
      ) : (
        <NoImage className={s.noimage} />
      )}
      <div className={s.metadata}>
        <span className={s.metadata__name}>{place.name}</span>
        <span className={s.metadata__address}>{place.address}</span>
        <div className={s.metadata__tags}>
          {review.tags &&
            review.tags.map((tag) => (
              <Tag key={tag.id} tag={tag} className={s.metadata__tag} />
            ))}
        </div>
      </div>
    </div>
  );
}
