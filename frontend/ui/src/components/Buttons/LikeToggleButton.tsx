import cn from 'classnames';

import { useLikeMutation } from '@/hooks/mutation';

import { HeartIcon } from '@/components/Icons';

import s from './Button.module.scss';
import { useToggleButton } from './useToggleButton';

interface Props {
  isLiked?: boolean;
  itemType: 'reviews' | 'collections';
  itemId: string | number;
  size?: number;
  likeCount: number;
}

export default function LikeToggleButton({
  isLiked: initialIsLiked = false,
  likeCount: initialLikeCount,
  itemType,
  itemId,
  size = 32,
  className,
  ...props
}: React.ComponentProps<'button'> & Props) {
  const {
    toggle,
    count: likeCount,
    enabled: isLiked,
  } = useToggleButton({
    enabled: initialIsLiked,
    count: initialLikeCount,
  });
  const mutation = useLikeMutation();

  const handleClick = () => {
    const reset = toggle();

    mutation.mutate(
      {
        type: itemType,
        id: itemId,
        method: isLiked ? 'delete' : 'post',
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
      <HeartIcon
        className={cn(s.icon, s.fill)}
        width={size}
        height={size}
        isFilled={isLiked}
      />
      <span>{likeCount}</span>
    </button>
  );
}
