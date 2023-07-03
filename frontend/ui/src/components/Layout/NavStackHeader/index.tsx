import cn from 'classnames';
import { useRouter } from 'next/router';

import { ArrowIcon } from '@/components/Icons';

import s from './NavStackHeader.module.scss';

interface NavStackHeaderProps {
  children?: React.ReactNode;
  hideTitle?: boolean;
  backButton?: boolean;
}

export default function NavStackHeader({
  children,
  hideTitle,
  backButton = true,
}: NavStackHeaderProps) {
  const router = useRouter();

  let backButtonElement = null;
  if (backButton) {
    backButtonElement = (
      <button className={s.root__backButton} onClick={router.back}>
        <ArrowIcon />
      </button>
    );
  }

  return (
    <div className={cn(s.root, { [s.transparent]: hideTitle })}>
      {backButtonElement}
      <p
        className={cn(s.root__title, {
          [s.hidden]: hideTitle,
          [s.paddingRight]: backButton,
        })}
      >
        {children}
      </p>
    </div>
  );
}
