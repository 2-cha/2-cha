import { useState } from 'react';
import cn from 'classnames';

import { useLikeMutation } from '@/hooks/mutation/useLike';

import HeartIcon from '../Icons/HeartIcon';

import s from './Button.module.scss';

interface Props {
  isLiked?: boolean;
  itemType: 'reviews' | 'collections';
  itemId: string | number;
  size?: number;
  nOfLikes: number;
}

export default function LikeToggleButton({
  isLiked: initialIsLiked = false,
  itemType,
  itemId,
  size = 32,
  nOfLikes,
  className,
  ...props
}: React.ComponentProps<'button'> & Props) {
  const [isLiked, setLiked] = useState(initialIsLiked);
  const mutation = useLikeMutation();

  const handleClick = () => {
    setLiked((prev) => !prev);
    mutation.mutate(
      {
        type: itemType,
        id: itemId,
        method: isLiked ? 'delete' : 'post',
      },
      { onError: () => setLiked((prev) => !prev) }
    );
  };

  return (
    <button
      className={cn(s.container, className)}
      onClick={handleClick}
      {...props}
    >
      <HeartIcon
        className={cn(s.icon)}
        width={size}
        height={size}
        isFilled={isLiked}
      />
      <span>{nOfLikes}</span>
    </button>
  );
}
