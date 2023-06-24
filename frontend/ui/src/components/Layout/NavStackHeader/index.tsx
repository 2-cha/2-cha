import cn from 'classnames';
import { useRouter } from 'next/router';

import { ArrowIcon } from '@/components/Icons';

import s from './NavStackHeader.module.scss';

interface NavStackHeaderProps {
  children?: React.ReactNode;
  hideTitle?: boolean;
}

export default function NavStackHeader({
  children,
  hideTitle,
}: NavStackHeaderProps) {
  const router = useRouter();

  return (
    <div className={cn(s.root, { [s.transparent]: hideTitle })}>
      <button className={s.root__backButton} onClick={router.back}>
        <ArrowIcon />
      </button>
      <p className={cn(s.root__title, { [s.hidden]: hideTitle })}>{children}</p>
    </div>
  );
}
