import { useCallback, useState } from 'react';

import Tab from '@/components/Tab';

import { useMemberReviewsQuery } from '@/hooks/query';
import { useIntersection } from '@/hooks';
import ReviewList from './ReviewList';

import s from './ProfileReviewTab.module.scss';

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
          <>
            {data?.pages[0]?.length ? (
              <>
                <ReviewList pages={data.pages} />
                <div ref={ref} aria-hidden style={{ height: 1 }} />
                {isFetching && <div>Loading...</div>}
              </>
            ) : (
              <p className={s.hasNoReview}>아직 리뷰가 없습니다</p>
            )}
          </>
        ) : (
          <span>공사중</span>
        )}
      </div>
    </div>
  );
}
