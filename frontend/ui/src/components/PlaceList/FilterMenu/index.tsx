import { useScrollDirection } from '@/hooks/useScrollDirection';
import cn from 'classnames';
import s from './FilterMenu.module.scss';

export default function FilterMenu() {
  const scrollDirection = useScrollDirection({ offset: 3 });

  return (
    <div className={cn(s.container, { [s.hide]: scrollDirection === 'down' })}>
      <div className={s.item}>filter</div>
      <div className={s.item}>sort</div>
    </div>
  );
}
