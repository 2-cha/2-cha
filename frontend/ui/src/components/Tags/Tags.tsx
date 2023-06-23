import { useCallback, useState } from 'react';
import cn from 'classnames';

import { SimpleArrowIcon } from '@/components/Icons';
import { Tag } from '@/types';

import s from './Tags.module.scss';

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
      className={cn(s.tags, {
        className,
        [s.gradient]: !isOpen && tagProperty.hasLimit,
        [s.padding]: tagProperty.hasLimit,
      })}
    >
      <button
        type="button"
        className={cn(s.button, {
          [s.hidden]: !tagProperty.hasLimit,
          [s.flipped]: isOpen,
        })}
        onClick={handleClickButton}
      >
        <SimpleArrowIcon />
      </button>
      <div className={s.tagWrapper}>
        {tagProperty.topTags.map((tag, index) => (
          <div key={`tag-list-${keyID}-${index}`} className={s.tag}>
            <span>
              {tag.emoji} {tag.message}
            </span>
            <span className={cn({ [s.hidden]: !isNumberShown })}>
              {tag.count}
            </span>
          </div>
        ))}
        {tagProperty.bottomTags.map((tag, index) => (
          <div
            key={`tag-list-${keyID}-${index + limit}`}
            className={cn({ [s.tag]: true, [s.hidden]: !isOpen })}
          >
            <span>
              {tag.emoji} {tag.message}
            </span>
            <span className={cn({ [s.hidden]: !isNumberShown })}>
              {tag.count}
            </span>
          </div>
        ))}
      </div>
    </div>
  );
}
