import Image from 'next/image';
import Link from 'next/link';
import { Fragment } from 'react';

import ImageSlide from '@/components/ImageSlide';
import { Tags } from '@/components/Tags';
import type { Review } from '@/types';

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
  return (
    <div className={s.reviewItem}>
      <div className={s.header}>
        <div className={s.header__profile}>
          <Image
            width={50}
            height={50}
            src={review.member.prof_img}
            alt={review.member.name}
          />
        </div>
        <Link href={`/member/${review.member.id}`}>
          <p className={s.review__title}>{review.member.name}</p>
        </Link>
      </div>
      <ImageSlide
        className={s.image}
        images={review.images}
        alt={`${review.place.name}-${review.member.name}`}
      />
      <Tags limit={3} keyID={review.id.toString()} tagList={review.tags} />
    </div>
  );
}
