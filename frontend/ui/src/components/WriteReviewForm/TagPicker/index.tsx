import { useEffect, useState } from 'react';
import { useTagsQuery } from '@/hooks/query/useTags';
import { useForm, useFormContext } from 'react-hook-form';
import { debounce } from '@/lib/debounce';
import type { Tag } from '@/types';
import type { ReviewFormData } from '@/components/WriteReviewForm';
import cn from 'classnames';
import s from './TagPicker.module.scss';

interface TagPickerProps {
  name: keyof ReviewFormData;
}

export default function TagPicker({ name }: TagPickerProps) {
  const [selected, setSelected] = useState<Tag[]>([]);
  const { register, setValue } = useFormContext<ReviewFormData>();
  register(name, { required: true });

  useEffect(() => {
    setValue(name, selected);
  }, [selected, name, setValue]);

  const toggleSelect = (tag: Tag) => {
    if (selected.find((t) => t.id === tag.id)) {
      setSelected(selected.filter((t) => t.id !== tag.id));
    } else {
      setSelected([...selected, tag]);
    }
  };

  return (
    <>
      <div className={s.tagContainer}>
        {selected.map((tag) => (
          <button
            key={tag.id}
            className={s.tag}
            onClick={() => toggleSelect(tag)}
          >
            {tag.emoji} {tag.message}
          </button>
        ))}
      </div>
      <TagSearchForm selected={selected} toggleSelect={toggleSelect} />
    </>
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
  const { register, handleSubmit, resetField } = useForm<TagFormData>();

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
    <>
      <form onSubmit={onSubmit} className={s.form}>
        <div>
          <label htmlFor="name">태그 추가</label>
          <input
            {...register('name')}
            type="text"
            placeholder="초성으로 태그를 검색하세요"
            onChange={handleChange}
            onClick={() => resetField('name')}
          />
        </div>
      </form>
      <ul className={s.searchResults}>
        {tags && !isError
          ? tags.map((tag) => (
              <li key={tag.id}>
                <button
                  className={cn({
                    [s.selected]: selected.find((t) => t.id === tag.id),
                  })}
                  onClick={() => toggleSelect(tag)}
                >
                  {tag.emoji} {tag.message}
                </button>
              </li>
            ))
          : null}
      </ul>
    </>
  );
}
