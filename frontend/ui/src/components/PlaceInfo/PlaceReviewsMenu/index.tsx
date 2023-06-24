import Image from 'next/image';
import Link from 'next/link';
import { useCallback, Fragment } from 'react';

import { useIntersection } from '@/hooks';
import { usePlaceReviewsQuery } from '@/hooks/query';
import type { Review } from '@/types';
import { Tags } from '@/components/Tags';

import s from './PlaceReviewsMenu.module.scss';

export default function PlaceReviews({ placeId }: { placeId: number }) {
  const { data, fetchNextPage, isFetching, isLoading, isError } =
    usePlaceReviewsQuery(placeId);

  const handleNextPage = useCallback(
    (isIntersecting: boolean) => isIntersecting && fetchNextPage(),
    [fetchNextPage]
  );
  const { ref } = useIntersection({ onChange: handleNextPage });

  // TODO: 이미지 슬라이드
  return (
    <div className={s.root}>
      {data?.pages[0]?.length ? (
        <>
          <PlaceReviewList pages={data.pages} />
          <div ref={ref} aria-hidden style={{ height: 1 }} />
          {isFetching && <div>Loading...</div>}
        </>
      ) : (
        <p className={s.hasNoReview}>아직 리뷰가 없습니다</p>
      )}
    </div>
  );
}

function PlaceReviewList({ pages }: { pages: Review[][] }) {
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
      <div className={s.image}>
        <Image
          width={480}
          height={480}
          src={review.images[0]}
          alt={`${review.place.name}-${review.id}`}
        />
      </div>
      <Tags limit={3} keyID={review.id.toString()} tagList={review.tags} />
    </div>
  );
}
