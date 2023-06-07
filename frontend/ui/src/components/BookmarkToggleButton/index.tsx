import * as React from 'react';
import { useState } from 'react';
import cn from 'classnames';
import s from './BookmarkToggleButton.module.scss';
import { useBookmakrMutation } from '@/hooks/mutation/useBookmark';
import BookmarkIcon from '@/components/Icons/BookmarkIcon';

interface BookmarkButtonProps {
  isBookmarked?: boolean;
  itemType: string;
  itemId: string | number;
  size?: number;
}

export function BookmarkToggleButton({
  isBookmarked: initialIsBookmarked = false,
  itemType,
  itemId,
  size = 32,
  className,
  ...props
}: React.ComponentProps<'button'> & BookmarkButtonProps) {
  const [isBookmarked, setBookmarked] = useState(initialIsBookmarked);
  const mutation = useBookmakrMutation();

  const handleClick = () => {
    const prevIsBookmarked = isBookmarked;

    setBookmarked(!isBookmarked);
    mutation.mutate(
      {
        type: itemType,
        id: itemId,
        method: isBookmarked ? 'delete' : 'post',
      },
      { onError: () => setBookmarked(prevIsBookmarked) }
    );
  };

  return (
    <button
      className={cn(s.container, className)}
      onClick={handleClick}
      {...props}
    >
      <BookmarkIcon
        width={size}
        height={size}
        isSingle
        isActive={isBookmarked}
      />
    </button>
  );
}
