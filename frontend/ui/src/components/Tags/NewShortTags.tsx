import { useCallback, useState } from 'react';
import cn from 'classnames';

import { Tag } from '@/types';

import s from './NewShortTags.module.scss';

interface Props {
  tagList: Tag[];
  keyID: string;
  limit?: number;
  className?: string;
}

export default function NewShortTags({ tagList, keyID, className }: Props) {
  const [shownTooltipIndex, setShownTooltipIndex] = useState<number>(-1);

  const handleOnHover = useCallback(function (index: number) {
    return () => setShownTooltipIndex(index);
  }, []);

  const handleOnBlur = useCallback(function () {
    return () => setShownTooltipIndex(-1);
  }, []);

  return (
    <div className={cn(className, s.tagWrapper)}>
      {tagList.map((tag, index) => (
        <div
          className={s.tag}
          key={`tag-ist-${keyID}-${index}`}
          onFocus={handleOnHover(index)}
          onMouseOver={handleOnHover(index)}
          onBlur={handleOnBlur()}
          onMouseOut={handleOnBlur()}
        >
          <div className={s.tagInner}>
            <span>
              {tag.emoji} {tag.count}
            </span>
          </div>
          {shownTooltipIndex === index && (
            <div className={s.tooltip}>
              <span>{tag.message}</span>
            </div>
          )}
        </div>
      ))}
    </div>
  );
}
