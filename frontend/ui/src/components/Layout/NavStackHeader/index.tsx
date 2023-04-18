import ArrowIcon from '@/components/Icons/ArrowIcon';
import { useRouter } from 'next/router';
import cn from 'classnames';
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
    <div className={s.root}>
      <button className={s.root__backButton} onClick={router.back}>
        <ArrowIcon />
      </button>
      <p className={cn(s.root__title, { [s.hidden]: hideTitle })}>{children}</p>
    </div>
  );
}