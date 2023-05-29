import { useCallback, useState } from 'react';

import styles from './ProfileReviewTab.module.scss';

export default function ProfileReviewTab() {
  const [selectedTabIndex, setSelectedTabIndex] = useState(0);

  const handleClickReviewButton = useCallback(() => {
    setSelectedTabIndex(0);
  }, [setSelectedTabIndex]);

  const handleClickMapButton = useCallback(() => {
    setSelectedTabIndex(1);
  }, [setSelectedTabIndex]);

  return (
    <div className={styles.root}>
      <div className={styles.tabsection}>
        <button
          type="button"
          className={`${styles.button} ${
            selectedTabIndex === 0 ? styles.selected : ''
          }`}
          onClick={handleClickReviewButton}
        >
          <span>리뷰</span>
        </button>
        <button
          type="button"
          className={`${styles.button} ${
            selectedTabIndex === 1 ? styles.selected : ''
          }`}
          onClick={handleClickMapButton}
        >
          <span>지도</span>
        </button>
      </div>
      <div className={styles.reviewsection}>
        <span>{selectedTabIndex === 0 ? '작성한 리뷰' : '지도'}</span>
      </div>
    </div>
  );
}
