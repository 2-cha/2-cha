import { useCallback, useState } from 'react';

import Tab from '@/components/Tab';

import s from './ProfileReviewTab.module.scss';
import { useMemberReviewsQuery } from '@/hooks/query/useMemberReviews';
import ReviewList from './ReviewList';
import { useIntersection } from '@/hooks/useIntersection';

const menuItems = ['리뷰', '지도'];

interface Props {
  memberId: number;
}

export default function ProfileReviewTab({ memberId }: Props) {
  const [selectedTabIndex, setSelectedTabIndex] = useState(0);
  const { data, fetchNextPage, isFetching, isLoading, isError } =
    useMemberReviewsQuery(memberId);

  const handleNextPage = useCallback(
    (isIntersecting: boolean) => isIntersecting && fetchNextPage(),
    [fetchNextPage]
  );

  const { ref } = useIntersection({ onChange: handleNextPage });

  return (
    <div className={s.root}>
      <Tab
        menuList={menuItems}
        currentIndex={selectedTabIndex}
        setCurrentIndex={setSelectedTabIndex}
      />
      <div className={s.review}>
        {selectedTabIndex === 0 ? (
          <div className={s.review__section}>
            {data?.pages[0]?.length ? (
              <>
                <ReviewList pages={data.pages} />
                <div ref={ref} aria-hidden style={{ height: 1 }} />
                {isFetching && <div>Loading...</div>}
              </>
            ) : (
              <p className={s.hasNoReview}>아직 리뷰가 없습니다</p>
            )}
          </div>
        ) : (
          <span>지도</span>
        )}
      </div>
    </div>
  );
}
