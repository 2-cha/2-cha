import Drawer from '@/components/Layout/Drawer';
import TagPicker from '@/components/TagPicker';
import { useEffect, useState } from 'react';
import { useRecoilState, useRecoilValue, useSetRecoilState } from 'recoil';
import { useModal } from '@/hooks/useModal';
import { useTagPicker } from '@/hooks/useTagPicker';
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
      <TagFilterMenu />
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
  const placeFilterBy = useRecoilValue(placeFilterByState);

  useEffect(() => {
    const sortBy = sortOption[Object.keys(sortOption)[sort]];

    // 태그가 많은 순 정렬은 태그 필터가 있을 때만 가능
    // TODO: 태그 필터가 없을 때 메뉴 비활성화
    if (placeFilterBy?.filter_by !== 'tag' && sortBy.sort_by === 'tag_count') {
      setSort(0);
      return;
    }

    setPlaceSortBy(sortBy);
  }, [sort, setPlaceSortBy, placeFilterBy]);

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

function TagFilterMenu() {
  const { isOpen, onOpen, onClose } = useModal();
  const { selected, setSelected, toggleSelect } = useTagPicker();

  const [placeFilterBy, setPlaceFilterBy] = useRecoilState(placeFilterByState);

  // 태그가 아닌 다른 필터가 선택되면 TagPicker를 초기화
  useEffect(() => {
    if (placeFilterBy && placeFilterBy.filter_by !== 'tag') {
      setSelected([]);
    }
  }, [placeFilterBy, setSelected]);

  const handleSubmit = () => {
    setPlaceFilterBy({
      filter_by: 'tag',
      filter_values: selected.map((tag) => tag.id).map(String),
    });
    onClose();
  };

  return (
    <>
      <button className={s.item} onClick={onOpen}>
        <span>#태그</span>
        {selected.length > 0 ? <span> {selected.length}</span> : null}
      </button>
      <Drawer
        isOpen={isOpen}
        onClose={onClose}
        header={<p className={s.modal__header}>태그</p>}
      >
        <div className={s.scrollable}>
          <TagPicker selected={selected} toggleSelect={toggleSelect} />
          <button
            className={s.submit}
            onClick={handleSubmit}
            disabled={selected.length === 0}
          >
            선택
          </button>
        </div>
      </Drawer>
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
