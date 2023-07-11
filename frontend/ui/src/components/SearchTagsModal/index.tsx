import cn from 'classnames';

import Drawer from '@/components/Layout/Drawer';
import TagPicker from '@/components/TagPicker';
import { type Tag } from '@/types';

import s from './SearchTagsModal.module.scss';

interface SearchTagsModalProps {
  isOpen: boolean;
  onClose: () => void;

  onSubmit?: (selected: Tag[]) => void;
  onReset?: () => void;

  selected: Tag[];
  setSelected: (selected: Tag[]) => void;
  toggleSelect: (tag: Tag) => void;
}

export default function SearchTagsModal({
  isOpen,
  onClose,

  onSubmit,
  onReset,

  selected,
  setSelected,
  toggleSelect,
}: SearchTagsModalProps) {
  const handleSubmit = () => {
    onSubmit?.(selected);
    onClose();
  };

  const handleReset = () => {
    setSelected([]);
    onReset?.();
    onClose();
  };

  return (
    <Drawer
      isOpen={isOpen}
      onClose={onClose}
      header={<p className={s.header}>태그</p>}
    >
      <div className={s.scrollable}>
        <TagPicker
          selected={selected}
          toggleSelect={toggleSelect}
          className={s.tagPicker}
          resultClassName={s.tagPicker__result}
        />
        <button
          className={cn(s.submit, { [s.disabled]: selected.length === 0 })}
          onClick={selected.length > 0 ? handleSubmit : handleReset}
        >
          선택
        </button>
        <button className={s.reset} onClick={handleReset}>
          초기화
        </button>
      </div>
    </Drawer>
  );
}
