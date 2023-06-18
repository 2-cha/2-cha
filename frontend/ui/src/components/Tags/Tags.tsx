import { useCallback, useState } from 'react';
import cn from 'classnames';

import SimpleArrowIcon from '@/components/Icons/SimpleArrowIcon';
import { Tag } from '@/types';

import styles from './Tags.module.scss';

interface Props {
  tagList: Tag[];
  keyID: string;
  limit?: number;
  className?: string;
  isNumberShown?: boolean;
}

interface TagProperty {
  hasLimit: boolean;
  topTags: Tag[];
  bottomTags: Tag[];
}

export default function Tags({
  tagList,
  keyID,
  limit = 0,
  className,
  isNumberShown,
}: Props) {
  const [isOpen, setIsOpen] = useState(false);

  const tagProperty: TagProperty = {
    hasLimit: false,
    topTags: tagList,
    bottomTags: [],
  };

  if (limit > 0 && limit < tagList.length) {
    tagProperty.hasLimit = true;
    tagProperty.topTags = tagList.slice(0, limit);
    tagProperty.bottomTags = tagList.slice(limit);
  }

  const handleClickButton = useCallback(
    function () {
      setIsOpen((prevState) => !prevState);
    },
    [setIsOpen]
  );

  return (
    <div
      className={cn(styles.tags, {
        className,
        [styles.gradient]: !isOpen && tagProperty.hasLimit,
        [styles.padding]: tagProperty.hasLimit,
      })}
    >
      <button
        type="button"
        className={cn(styles.button, {
          [styles.hidden]: !tagProperty.hasLimit,
          [styles.flipped]: isOpen,
        })}
        onClick={handleClickButton}
      >
        <SimpleArrowIcon />
      </button>
      <div className={styles.tagWrapper}>
        {tagProperty.topTags.map((tag, index) => (
          <div key={`tag-list-${keyID}-${index}`} className={styles.tag}>
            <span>
              {tag.emoji} {tag.message}
            </span>
            <span className={cn({ [styles.hidden]: !isNumberShown })}>
              {tag.count}
            </span>
          </div>
        ))}
        {tagProperty.bottomTags.map((tag, index) => (
          <div
            key={`tag-list-${keyID}-${index + limit}`}
            className={cn({ [styles.tag]: true, [styles.hidden]: !isOpen })}
          >
            <span>
              {tag.emoji} {tag.message}
            </span>
            <span className={cn({ [styles.hidden]: !isNumberShown })}>
              {tag.count}
            </span>
          </div>
        ))}
      </div>
    </div>
  );
}
