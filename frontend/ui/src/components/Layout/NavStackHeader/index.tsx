import ArrowIcon from '@/components/Icons/ArrowIcon';
import { useRouter } from 'next/router';
import s from './NavStackHeader.module.scss';

interface NavStackHeaderProps {
  children?: React.ReactNode;
}

export default function NavStackHeader({ children }: NavStackHeaderProps) {
  const router = useRouter();

  return (
    <div className={s.root}>
      <button className={s.root__backButton} onClick={router.back}>
        <ArrowIcon />
      </button>
      <div className={s.root__title}>{children}</div>
    </div>
  );
}
