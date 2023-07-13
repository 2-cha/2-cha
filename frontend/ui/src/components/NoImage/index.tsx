import cn from 'classnames';

import { SadIcon } from '@/components/Icons';

import s from './NoImage.module.scss';

interface Props {
  className?: string;
  withTitle?: boolean;
  subtitle?: string;
  iconSize?: number;
}

export default function NoImage({
  className,
  withTitle,
  subtitle,
  iconSize = 100,
}: Props) {
  return (
    <div className={cn(s.skeleton, className)}>
      <SadIcon
        className={cn({ [s.withTitle]: withTitle })}
        width={iconSize}
        height={iconSize}
      />
      {withTitle && (
        <span className={s.skeleton__title}>사진을 찾을 수 없어요</span>
      )}
      {subtitle != null && (
        <span className={s.skeleton__subtitle}>{subtitle}</span>
      )}
    </div>
  );
}
