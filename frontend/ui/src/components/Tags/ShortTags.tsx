import cx from 'classnames';
import { Tag } from '@/types';

import styles from './ShortTags.module.scss';
import { useCallback, useState } from 'react';

interface Props {
  tagList: Tag[];
  keyID: string;
  limit?: number;
  className?: string;
}

export default function ShortTags({ tagList, keyID, className }: Props) {
  const [shownTooltipIndex, setShownTooltipIndex] = useState<number>(-1);

  const handleOnHover = useCallback(function (index: number) {
    return () => setShownTooltipIndex(index);
  }, []);

  const handleOnBlur = useCallback(function () {
    return () => setShownTooltipIndex(-1);
  }, []);

  return (
    <div className={cx(className, styles.tagWrapper)}>
      {tagList.map((tag, index) => (
        <div
          className={styles.tag}
          key={`tag-ist-${keyID}-${index}`}
          onFocus={handleOnHover(index)}
          onMouseOver={handleOnHover(index)}
          onBlur={handleOnBlur()}
          onMouseOut={handleOnBlur()}
        >
          <div className={styles.tagInner}>
            <span>
              {tag.emoji} {tag.count}
            </span>
          </div>
          {shownTooltipIndex === index && (
            <div className={styles.tooltip}>
              <span>{tag.message}</span>
            </div>
          )}
        </div>
      ))}
    </div>
  );
}
