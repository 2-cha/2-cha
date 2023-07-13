import { Fragment } from 'react';

import ImageSlide from '@/components/ImageSlide';
import { Tags } from '@/components/Tags';
import { DeleteButton, ProfileButton } from '@/components/Buttons';
import type { Review } from '@/types';
import { useAuth } from '@/hooks';

import s from './PlaceReviewList.module.scss';

export default function PlaceReviewList({ pages }: { pages: Review[][] }) {
  return (
    <ul className={s.review__list}>
      {pages.map((page, idx) => (
        <Fragment key={idx}>
          {page.map((review) => (
            <li key={review.id} className={s.review__item}>
              <PlaceReviewItem review={review} />
            </li>
          ))}
        </Fragment>
      ))}
    </ul>
  );
}

function PlaceReviewItem({ review }: { review: Review }) {
  const { user } = useAuth();
  const memberId = user?.sub;

  return (
    <div className={s.reviewItem}>
      <div className={s.header}>
        <ProfileButton imageSize={50} member={review.member} />
        {review.member && memberId === String(review.member.id) && (
          <DeleteButton itemType="reviews" itemId={review.id} />
        )}
      </div>
      <ImageSlide
        className={s.image}
        images={review.images}
        alt={`${review.place.name}-${review.member?.name ?? 'unknwon'}`}
      />
      <Tags limit={3} keyID={review.id.toString()} tagList={review.tags} />
    </div>
  );
}
