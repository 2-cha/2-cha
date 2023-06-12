import { useState } from 'react';
import type { Tag } from '@/types';

export function useTagPicker() {
  const [selected, setSelected] = useState<Tag[]>([]);

  const toggleSelect = (tag: Tag) => {
    if (selected.find((t) => t.id === tag.id)) {
      setSelected(selected.filter((t) => t.id !== tag.id));
    } else {
      setSelected([...selected, tag]);
    }
  };

  return { selected, toggleSelect };
}
