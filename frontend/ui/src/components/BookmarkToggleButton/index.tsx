import * as React from 'react';
import { useState } from 'react';
import cn from 'classnames';
import s from './BookmarkToggleButton.module.scss';
import { useBookmakrMutation } from '@/hooks/mutation/useBookmark';

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
      {isBookmarked ? (
        <FilledBookmarkIcon size={size} />
      ) : (
        <StrokeBookmarkIcon size={size} />
      )}
    </button>
  );
}

function StrokeBookmarkIcon({ size }: { size: number }) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width={size}
      height={size}
      viewBox="0 0 16 16"
    >
      <path
        fill="currentColor"
        d="M2 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v13.5a.5.5 0 0 1-.777.416L8 13.101l-5.223 2.815A.5.5 0 0 1 2 15.5V2zm2-1a1 1 0 0 0-1 1v12.566l4.723-2.482a.5.5 0 0 1 .554 0L13 14.566V2a1 1 0 0 0-1-1H4z"
      />
    </svg>
  );
}

function FilledBookmarkIcon({ size }: { size: number }) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width={size}
      height={size}
      viewBox="0 0 16 16"
    >
      <path
        fill="currentColor"
        d="M2 2v13.5a.5.5 0 0 0 .74.439L8 13.069l5.26 2.87A.5.5 0 0 0 14 15.5V2a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2z"
      />
    </svg>
  );
}
