import { ComponentProps, useState } from 'react';
import cn from 'classnames';

import { useFollowMutation } from '@/hooks/mutation';
import { CheckIcon, PlusSquareIcon } from '../Icons';

import s from './FollowToggleButton.module.scss';

interface FollowButtonProps {
  isFollowed?: boolean;
  userId: string | number;
  isFollowCountShown?: boolean;
}

export default function FollowToggleButton({
  isFollowed: initialIsFollowed = false,
  userId,
  isFollowCountShown = false,
  className,
  ...props
}: ComponentProps<'button'> & FollowButtonProps) {
  const [isFollowed, setFollowed] = useState(initialIsFollowed);
  const mutation = useFollowMutation();

  const handleClick = () => {
    const prevIsFollowed = isFollowed;
    setFollowed(!isFollowed);
    mutation.mutate(
      {
        id: userId,
        method: isFollowed ? 'delete' : 'post',
      },
      { onError: () => setFollowed(prevIsFollowed) }
    );
  };

  return (
    <button
      className={cn(s.container, className)}
      onClick={handleClick}
      {...props}
    >
      {isFollowed ? <CheckIcon /> : <PlusSquareIcon withoutBorder />}
      <span>{isFollowed ? '팔로잉' : '팔로우'}</span>
    </button>
  );
}
