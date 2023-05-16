import * as React from 'react';
import { useCallback } from 'react';
import { useIntersection } from '@/hooks/useIntersection';
import { usePlaceReviewsQuery } from '@/hooks/query/usePlaceReviews';
import Link from 'next/link';
import type { Review } from '@/types';
import cn from 'classnames';
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
          <Link
            href={`/write?placeId=${placeId}`}
            className={cn(s.review__item, s.review__writeButton)}
          >
            후기 남기기
          </Link>
          <PlaceReviewList pages={data.pages} />
          <div ref={ref} aria-hidden style={{ height: 1 }} />
          {isFetching && <div>Loading...</div>}
        </>
      ) : (
        <>
          <p className={s.hasNoReview}>아직 리뷰가 없습니다</p>
          <Link
            href={`/write?placeId=${placeId}`}
            className={s.hasNoReview__writeButton}
          >
            후기 남기기
          </Link>
        </>
      )}
    </div>
  );
}

function PlaceReviewList({ pages }: { pages: Review[][] }) {
  return (
    <ul className={s.review__list}>
      {pages.map((page, idx) => (
        <React.Fragment key={idx}>
          {page.map((review) => (
            <li key={review.id} className={s.review__item}>
              <PlaceReviewItem review={review} />
            </li>
          ))}
        </React.Fragment>
      ))}
    </ul>
  );
}

function PlaceReviewItem({ review }: { review: Review }) {
  return (
    <>
      <div className={s.header}>
        <div className={s.header__profile}>
          <img src={review.member.prof_img} alt={review.member.name} />
        </div>
        <Link href={`/member/${review.member.id}`}>
          <p className={s.review__title}>{review.member.name}</p>
        </Link>
      </div>
      <div className={s.review__image}>
        <img src={review.images[0]} alt={`${review.place.name}-${review.id}`} />
      </div>
      <div className={s.tags}>
        {review.tags.map((tag) => (
          <div key={tag.id} className={s.tag}>
            <span>
              {tag.emoji} {tag.message}
            </span>
          </div>
        ))}
      </div>
    </>
  );
}
