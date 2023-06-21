import { useState } from 'react';
import { useCategorizedTagsQuery } from '@/hooks/query/useCategorizedTags';
import { useForm } from 'react-hook-form';
import { debounce } from '@/lib/debounce';
import HashIcon from '../Icons/HashIcon';
import List from '../Layout/List';
import type { Tag } from '@/types';
import cn from 'classnames';
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
          placeholder="태그"
          onChange={handleChange}
        />
      </form>

      <List className={cn(s.searchResults, resultClassName)}>
        {tags && !isError
          ? Object.entries(tags).map(([category, tags]) => (
              <li>
                <ul>
                  <List.Subheader>{category}</List.Subheader>
                  {tags.map((tag) => (
                    <List.Item
                      key={tag.id}
                      onClick={() => toggleSelect(tag)}
                      selected={selected.some((t) => t.id === tag.id)}
                    >
                      <span className={s.tag__imoji}>{tag.emoji}</span>
                      <span className={s.tag__message}>{tag.message}</span>
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
