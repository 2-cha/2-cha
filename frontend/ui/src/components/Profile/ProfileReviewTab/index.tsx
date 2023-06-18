import { useCallback, useState } from 'react';

import Tab from '@/components/Tab';

import s from './ProfileReviewTab.module.scss';

const menuItems = ['리뷰', '지도'];

export default function ProfileReviewTab() {
  const [selectedTabIndex, setSelectedTabIndex] = useState(0);

  return (
    <div className={s.root}>
      <Tab
        menuList={menuItems}
        currentIndex={selectedTabIndex}
        setCurrentIndex={setSelectedTabIndex}
      />
      <div className={s.reviewsection}>
        <span>{selectedTabIndex === 0 ? '작성한 리뷰' : '지도'}</span>
      </div>
    </div>
  );
}
