import {
  Dispatch,
  Fragment,
  SetStateAction,
  useCallback,
  useEffect,
} from 'react';
import cn from 'classnames';
import Link from 'next/link';
import Image from 'next/image';

import { Review } from '@/types';
import { useIntersection } from '@/hooks';
import { useMemberReviewsQuery } from '@/hooks/query';
import Drawer from '../Layout/Drawer';

import s from './AddReviews.module.scss';

interface Props {
  memberId: number;
  selectedReviews: number[];
  setSelectedReviews: Dispatch<SetStateAction<number[]>>;
  isOpen?: boolean;
  onClose?: () => void;
}

export default function AddReviews({
  memberId,
  selectedReviews,
  setSelectedReviews,
  isOpen = false,
  onClose,
}: Props) {
  const { data, fetchNextPage, isFetching, isLoading, isError } =
    useMemberReviewsQuery(memberId);

  const handleNextPage = useCallback(
    (isIntersecting: boolean) => isIntersecting && fetchNextPage(),
    [fetchNextPage]
  );
  const { ref } = useIntersection({ onChange: handleNextPage });

  const handleClickNextButton = useCallback(() => {
    if (!selectedReviews.length) return;
    onClose?.();
  }, [selectedReviews, onClose]);

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
    <Drawer isOpen={isOpen} onClose={onClose}>
      <div className={s.top}>
        <h2>리뷰 추가하기</h2>
        <button
          type="button"
          onClick={handleClickNextButton}
          className={cn(s.next, { [s.disabled]: !selectedReviews.length })}
        >
          <span>확인</span>
        </button>
      </div>
      {data?.pages[0]?.length ? (
        <>
          <ul className={s.container}>
            {data.pages.map((page, index) => (
              <Fragment key={index}>
                {page.map((review) => (
                  <li
                    key={review.id}
                    className={cn(s.element, {
                      [s.selected]: selectedReviews.includes(review.id),
                    })}
                  >
                    {selectedReviews.includes(review.id) ? (
                      <button
                        type="button"
                        onClick={handleClickRemoveButton(review.id)}
                        className={s.element__button}
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
    </Drawer>
  );
}
