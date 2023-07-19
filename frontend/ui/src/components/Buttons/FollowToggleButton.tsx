import { ComponentProps, useState } from 'react';
import cn from 'classnames';
import { FollowIcon } from '../Icons';
import { useFollowMutation } from '@/hooks/mutation';

import s from './Button.module.scss';

interface FollowButtonProps {
  isFollowed?: boolean;
  userId: string | number;
  isFollowCountShown?: boolean;
  size?: number;
}

export default function FollowToggleButton({
  isFollowed: initialIsFollowed = false,
  userId,
  isFollowCountShown = false,
  className,
  size = 32,
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
      {
        <FollowIcon
          width={size}
          height={size}
          isFollowed={isFollowed}
          isFilled={isFollowed}
          className={cn(s.icon, s.fill)}
        />
      }
      <span>&nbsp;</span>
    </button>
  );
}
