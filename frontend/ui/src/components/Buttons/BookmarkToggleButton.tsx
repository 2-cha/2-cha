import cn from 'classnames';

import { useBookmarkMutation } from '@/hooks/mutation';
import { BookmarkIcon } from '@/components/Icons';

import s from './Button.module.scss';
import { useToggleButton } from './useToggleButton';

interface BookmarkButtonProps {
  isBookmarked?: boolean;
  itemType: string;
  itemId: string | number;
  size?: number;
  bookmarkCount: number;
}

export default function BookmarkToggleButton({
  isBookmarked: initialIsBookmarked = false,
  bookmarkCount: initialBookmarkCount,
  itemType,
  itemId,
  size = 32,
  className,
  ...props
}: React.ComponentProps<'button'> & BookmarkButtonProps) {
  const {
    toggle,
    count: bookmarkCount,
    enabled: isBookmarked,
  } = useToggleButton({
    enabled: initialIsBookmarked,
    count: initialBookmarkCount,
  });
  const mutation = useBookmarkMutation();

  const handleClick = () => {
    const reset = toggle();

    mutation.mutate(
      {
        type: itemType,
        id: itemId,
        method: isBookmarked ? 'delete' : 'post',
      },
      { onError: reset }
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
      <span>{bookmarkCount}</span>
    </button>
  );
}
