import cn from 'classnames';

import { useDeleteMutation } from '@/hooks/mutation';
import { TrashIcon } from '../Icons';

import s from './DeleteButton.module.scss';
import { useRouter } from 'next/router';

interface Props {
  itemType?: 'collections' | 'reviews';
  itemId: string | number;
  size?: number;
  className?: string;
}

export default function DeleteButton({
  itemType = 'reviews',
  size = 24,
  itemId,
  className,
}: Props) {
  const mutation = useDeleteMutation();
  const router = useRouter();

  const handleClick = () => {
    if (confirm('정말 삭제하시겠습니까?')) {
      mutation.mutate({ type: itemType, id: itemId });
      router.push(`/${itemType === 'reviews' ? 'places' : itemType}`);
    } else return;
  };

  return (
    <button
      type="button"
      className={cn(s.button, className)}
      onClick={handleClick}
    >
      <TrashIcon width={size} height={size} className={s.button__icon} />
    </button>
  );
}
