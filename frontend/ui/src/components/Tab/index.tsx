import cn from 'classnames';

import s from './Tab.module.scss';

interface Props {
  menuList: string[];
  isSticky?: boolean;
  currentIndex: number;
  setCurrentIndex: (index: number) => void;
}

export default function Tab({
  menuList,
  isSticky,
  currentIndex,
  setCurrentIndex,
}: Props) {
  return (
    <div className={cn(s.menu, { [s.sticky]: isSticky })}>
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
