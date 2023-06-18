import cn from 'classnames';
import s from './Tab.module.scss';

interface Props {
  menuList: string[];
  currentIndex: number;
  setCurrentIndex: (index: number) => void;
}

export default function Tab({
  menuList,
  currentIndex,
  setCurrentIndex,
}: Props) {
  return (
    <div className={s.menu}>
      {menuList.map((menu, index) => (
        <button
          key={menu}
          className={cn(s.menu__item, {
            [s.menu__itemActive]: index === currentIndex,
          })}
          onClick={() => setCurrentIndex(index)}
        >
          {menu}
        </button>
      ))}
    </div>
  );
}
