import { useState } from 'react';
import cn from 'classnames';
import { useForm } from 'react-hook-form';

import { useCategorizedTagsQuery } from '@/hooks/query';
import { debounce } from '@/lib/debounce';
import type { Tag } from '@/types';
import { HashIcon } from '@/components/Icons';
import List from '../Layout/List';

import s from './TagPicker.module.scss';

interface TagPickerProps {
  selected: Tag[];
  toggleSelect: (tag: Tag) => void;
  className?: string;
  resultClassName?: string;
}

export default function TagPicker({
  selected,
  toggleSelect,
  className,
  resultClassName,
}: TagPickerProps) {
  return (
    <div className={cn(s.container, className)}>
      <TagSearchForm
        selected={selected}
        toggleSelect={toggleSelect}
        resultClassName={resultClassName}
      />
      <span>선택됨</span>
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
      </div>
    </div>
  );
}

interface TagFormData {
  name: string;
}

interface TagSearchFormProps {
  selected: Tag[];
  toggleSelect: (tag: Tag) => void;
  resultClassName?: string;
}

function TagSearchForm({
  selected,
  toggleSelect,
  resultClassName,
}: TagSearchFormProps) {
  const { register, handleSubmit } = useForm<TagFormData>();

  const [query, setQuery] = useState<string>('');
  const { data: tags, isError } = useCategorizedTagsQuery(query);

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
        <HashIcon />
        <input
          {...register('name')}
          className={s.form__input}
          type="search"
          placeholder="검색"
          onChange={handleChange}
          autoComplete="off"
        />
      </form>

      <List className={cn(s.searchResults, resultClassName)}>
        {tags && !isError
          ? Object.entries(tags).map(([category, tags]) => (
              <li key={category}>
                <ul>
                  <List.Subheader>{category}</List.Subheader>
                  {tags.map((tag) => (
                    <List.Item
                      key={tag.id}
                      onClick={() => toggleSelect(tag)}
                      selected={selected.some((t) => t.id === tag.id)}
                    >
                      <span className={s.tag__imoji}>{tag.emoji}</span>
                      <span className={s.tag__message}>
                        {tag.message.split('').map((char, idx) => (
                          <span
                            key={tag.message + idx}
                            className={cn({
                              [s.match]: tag.matching_indexes?.includes(idx),
                            })}
                          >
                            {char}
                          </span>
                        ))}
                      </span>
                    </List.Item>
                  ))}
                </ul>
              </li>
            ))
          : null}
      </List>
    </div>
  );
}
