import { useCallback } from 'react';

import { useIntersection } from '@/hooks';
import { usePlaceReviewsQuery } from '@/hooks/query';

import PlaceReviewList from './PlaceReviewList';
import s from './PlaceReviewsMenu.module.scss';

export default function PlaceReviews({ placeId }: { placeId: number }) {
  const { data, fetchNextPage, isFetching } = usePlaceReviewsQuery(placeId);

  const handleNextPage = useCallback(
    (isIntersecting: boolean) => isIntersecting && fetchNextPage(),
    [fetchNextPage]
  );
  const { ref } = useIntersection({ onChange: handleNextPage });

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
