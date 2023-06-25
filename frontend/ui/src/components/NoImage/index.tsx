import cn from 'classnames';

import { SadIcon } from '@/components/Icons';

import s from './NoImage.module.scss';

interface Props {
  className?: string;
}

export default function NoImage({ className }: Props) {
  return (
    <div className={cn(s.skeleton, className)}>
      <SadIcon width={100} height={100} />
      <span className={s.skeleton__title}>사진을 찾을 수 없어요</span>
      <span className={s.skeleton__subtitle}>첫 리뷰어가 되어보세요</span>
    </div>
  );
}
