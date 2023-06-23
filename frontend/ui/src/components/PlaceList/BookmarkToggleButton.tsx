import * as React from 'react';
import { useState } from 'react';
import cn from 'classnames';

import { useBookmarkMutation } from '@/hooks/mutation/useBookmark';
import BookmarkIcon from '@/components/Icons/BookmarkIcon';

import s from './BookmarkToggleButton.module.scss';

interface BookmarkButtonProps {
  isBookmarked?: boolean;
  itemType: string;
  itemId: string | number;
  size?: number;
  nOfBookmarks: number;
}

export default function BookmarkToggleButton({
  isBookmarked: initialIsBookmarked = false,
  itemType,
  itemId,
  size = 32,
  nOfBookmarks,
  className,
  ...props
}: React.ComponentProps<'button'> & BookmarkButtonProps) {
  const [isBookmarked, setBookmarked] = useState(initialIsBookmarked);
  const mutation = useBookmarkMutation();

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
        className={cn(s.icon, isBookmarked ? s.fill : s.empty)}
        width={size}
        height={size}
        isSingle
        isActive
      />
      <span>{nOfBookmarks}</span>
    </button>
  );
}
