import { useState } from 'react';
import { useTagsQuery } from '@/hooks/query/useTags';
import { useForm } from 'react-hook-form';
import { debounce } from '@/lib/debounce';
import type { Tag } from '@/types';
import cn from 'classnames';
import s from './TagPicker.module.scss';

interface TagPickerProps {
  selected: Tag[];
  toggleSelect: (tag: Tag) => void;
}

export default function TagPicker({ selected, toggleSelect }: TagPickerProps) {
  return (
    <div className={s.tagContainer}>
      {selected.map((tag) => (
        <button
          key={tag.id}
          type="button"
          className={s.tag}
          onClick={() => toggleSelect(tag)}
        >
          {tag.emoji} {tag.message}
        </button>
      ))}
      <TagSearchForm selected={selected} toggleSelect={toggleSelect} />
    </div>
  );
}

interface TagFormData {
  name: string;
}

interface TagSearchFormProps {
  selected: Tag[];
  toggleSelect: (tag: Tag) => void;
}

function TagSearchForm({ selected, toggleSelect }: TagSearchFormProps) {
  const { register, handleSubmit } = useForm<TagFormData>();

  const [query, setQuery] = useState<string>('');
  const { data: tags, isError } = useTagsQuery(query);

  const updateQuery = debounce(setQuery, 300);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    updateQuery(e.target.value);
  };
  const onSubmit = handleSubmit((data) => {
    updateQuery(data.name);
  });

  return (
    <div className={s.formContainer}>
      <form onSubmit={onSubmit} className={s.form}>
        <input
          {...register('name')}
          className={s.form__input}
          type="search"
          placeholder="초성으로 태그를 검색하세요"
          onChange={handleChange}
        />
      </form>

      <ul className={s.searchResults}>
        {tags && !isError
          ? tags.map((tag) => (
              <li key={tag.id} className={s.searchResults__item}>
                <button
                  type="button"
                  className={cn(s.tag, {
                    [s.selected]: selected.find((t) => t.id === tag.id),
                  })}
                  onClick={() => toggleSelect(tag)}
                >
                  <span className={s.tag__imoji}>{tag.emoji}</span>
                  <span>{tag.message}</span>
                </button>
              </li>
            ))
          : null}
      </ul>
    </div>
  );
}
