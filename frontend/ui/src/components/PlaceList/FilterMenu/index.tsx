import Drawer from '@/components/Layout/Drawer';
import { useEffect, useState } from 'react';
import { useSetRecoilState } from 'recoil';
import { useModal } from '@/hooks/useModal';
import { useScrollDirection } from '@/hooks/useScrollDirection';
import { getCategoryLabel } from '@/lib/placeUtil';
import {
  type SortBy,
  placeFilterByState,
  placeSortByState,
} from '@/atoms/placesQueryParams';
import cn from 'classnames';
import s from './FilterMenu.module.scss';

export default function SortFilterMenu() {
  const scrollDirection = useScrollDirection({ offset: 3 });

  return (
    <div className={cn(s.container, { [s.hide]: scrollDirection === 'down' })}>
      <SortMenu />
      <CategoryFilterMenu />
    </div>
  );
}

const sortOption: Record<string, SortBy> = {
  '가까운 순': { sort_by: 'distance', sort_order: 'asc' },
  '리뷰가 많은 순': { sort_by: 'review_count', sort_order: 'desc' },
  '태그가 많은 순': { sort_by: 'tag_count', sort_order: 'desc' },
};

function SortMenu() {
  const { isOpen, onClose, onOpen } = useModal();
  const [sort, setSort] = useState(0);
  const setPlaceSortBy = useSetRecoilState(placeSortByState);
  useEffect(() => {
    setPlaceSortBy(sortOption[Object.keys(sortOption)[sort]]);
  }, [sort, setPlaceSortBy]);

  return (
    <>
      <button className={s.item} onClick={onOpen}>
        {Object.keys(sortOption)[sort]}
      </button>
      <Menu
        title="정렬"
        isOpen={isOpen}
        onClose={onClose}
        onSelect={(idx) => setSort(idx)}
        menu={Object.keys(sortOption)}
      />
    </>
  );
}

const categoryOption = ['전체', 'WINE_BAR', 'COCKTAIL_BAR', 'WHISKEY_BAR'];

function CategoryFilterMenu() {
  const { isOpen, onClose, onOpen } = useModal();
  const [category, setCategory] = useState(0);
  const setPlaceFilterBy = useSetRecoilState(placeFilterByState);
  useEffect(() => {
    if (category === 0) {
      setPlaceFilterBy(null);
    } else {
      setPlaceFilterBy({
        filter_by: 'category',
        filter_values: [categoryOption[category]],
      });
    }
  }, [category, setPlaceFilterBy]);

  return (
    <>
      <button className={s.item} onClick={onOpen}>
        {category === 0
          ? '카테고리'
          : getCategoryLabel(categoryOption[category])}
      </button>
      <Menu
        title="카테고리"
        isOpen={isOpen}
        onClose={onClose}
        onSelect={(idx) => setCategory(idx)}
        menu={categoryOption.map((option) => getCategoryLabel(option))}
      />
    </>
  );
}

interface MenuProps {
  title: string;
  isOpen: boolean;
  onClose: () => void;
  onSelect: (idx: number) => void;
  menu?: string[];
}

function Menu({ title, isOpen, onClose, onSelect, menu }: MenuProps) {
  const handleClick = (idx: number) => {
    onSelect(idx);
    onClose();
  };

  return (
    <Drawer
      isOpen={isOpen}
      onClose={onClose}
      className={s.modal}
      header={<p className={s.modal__header}>{title}</p>}
    >
      <ul className={s.menuWrapper}>
        {menu?.map((item, idx) => (
          <li key={idx} className={s.menuItem__wrapper}>
            <button className={s.menuItem} onClick={() => handleClick(idx)}>
              {item}
            </button>
          </li>
        ))}
      </ul>
    </Drawer>
  );
}
