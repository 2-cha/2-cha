import Image from 'next/image';

import { Review } from '@/types';
import { Tag } from '@/components/Tags';
import NoImage from '@/components/NoImage';

import s from './ReviewCard.module.scss';

interface Props {
  review: Omit<Review, 'member'> & { image?: string };
}

export default function ReviewCard({ review }: Props) {
  const { place } = review;

  return (
    <>
      {review.image ? (
        <Image
          src={review.image}
          alt="collection image"
          width={480}
          height={480}
          unoptimized
          className={s.wrapper__image}
        />
      ) : (
        <NoImage className={s.noimage} withTitle />
      )}
      <div className={s.metadata}>
        <span className={s.metadata__name}>{place.name}</span>
        <span className={s.metadata__address}>{place.address}</span>
        <div className={s.metadata__tags}>
          {review.tags &&
            review.tags.map((tag) => (
              <Tag
                key={`${review.id}-${tag.message}`}
                tag={tag}
                className={s.metadata__tag}
              />
            ))}
        </div>
      </div>
    </>
  );
}
