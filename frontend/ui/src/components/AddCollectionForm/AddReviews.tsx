import {
  Dispatch,
  Fragment,
  SetStateAction,
  useCallback,
  useEffect,
} from 'react';
import cn from 'classnames';
import Image from 'next/image';

import { useIntersection } from '@/hooks';
import { useMemberReviewsQuery } from '@/hooks/query';

import s from './AddReviews.module.scss';

interface Props {
  memberId: number;
  selectedReviews: number[];
  setSelectedReviews: Dispatch<SetStateAction<number[]>>;
}

export default function AddReviews({
  memberId,
  selectedReviews,
  setSelectedReviews,
}: Props) {
  const { data, fetchNextPage, isFetching, isLoading, isError } =
    useMemberReviewsQuery(memberId);

  const handleNextPage = useCallback(
    (isIntersecting: boolean) => isIntersecting && fetchNextPage(),
    [fetchNextPage]
  );
  const { ref } = useIntersection({ onChange: handleNextPage });

  const handleClickAddButton = useCallback(
    (reviewId: number) => {
      return function () {
        setSelectedReviews((prev) => [...prev, reviewId]);
      };
    },
    [setSelectedReviews]
  );

  const handleClickRemoveButton = useCallback(
    (reviewId: number) => {
      return function () {
        const index = selectedReviews.indexOf(reviewId);
        setSelectedReviews((prev) => [
          ...prev.slice(0, index),
          ...prev.slice(index + 1),
        ]);
      };
    },
    [selectedReviews, setSelectedReviews]
  );

  useEffect(() => {
    console.log(selectedReviews);
  }, [selectedReviews]);

  return (
    <div className={s.root}>
      {data?.pages[0]?.length ? (
        <>
          <ul className={s.container}>
            {data.pages.map((page, index) => (
              <Fragment key={index}>
                {page.map((review) => (
                  <li key={review.id} className={cn(s.element)}>
                    {selectedReviews.includes(review.id) ? (
                      <button
                        type="button"
                        onClick={handleClickRemoveButton(review.id)}
                        className={cn(s.element__button, s.selected)}
                      >
                        <Image
                          src={review.images[0]}
                          alt={`${review.place} review`}
                          width={200}
                          height={200}
                        />
                        <div className={s.element__removeIcon}>
                          <span>{selectedReviews.indexOf(review.id) + 1}</span>
                        </div>
                      </button>
                    ) : (
                      <button
                        type="button"
                        onClick={handleClickAddButton(review.id)}
                        className={s.element__button}
                      >
                        <Image
                          src={review.images[0]}
                          alt={`${review.place} review`}
                          width={200}
                          height={200}
                        />
                        <div className={s.element__addIcon} />
                      </button>
                    )}
                  </li>
                ))}
              </Fragment>
            ))}
          </ul>
          <div ref={ref} aria-hidden style={{ height: 1 }} />
          {isFetching && <div>Loading...</div>}
        </>
      ) : (
        <p className={s.hasNoReview}>아직 리뷰가 없습니다</p>
      )}
    </div>
  );
}
