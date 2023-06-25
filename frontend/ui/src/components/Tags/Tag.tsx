import cn from 'classnames';

import { Tag as TagType } from '@/types';
import s from './Tag.module.scss';

interface Props {
  tag: TagType;
  isNumberShown?: boolean;
  className?: string;
}

export default function Tag({ tag, className, isNumberShown = false }: Props) {
  return (
    <div className={cn(s.tag, className)}>
      <span>
        {tag.emoji} {tag.message}
      </span>
      <span className={cn({ [s.hidden]: !isNumberShown })}>{tag.count}</span>
    </div>
  );
}
