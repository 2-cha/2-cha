import { useCallback, useEffect, useMemo, useState } from 'react';
import { useForm } from 'react-hook-form';
import cn from 'classnames';

import { useIntersection } from '@/hooks';
import { useSearchPlaceQuery } from '@/hooks/query';
import { getCategoryLabel } from '@/lib/placeUtil';
import Drawer from '@/components/Layout/Drawer';
import SearchInput from '@/components/SearchInput';
import List from '@/components/Layout/List';
import Link from 'next/link';
import type { Place, SuggestionPlace } from '@/types';

import s from './SearchPlaceModal.module.scss';

interface SearchPlaceFormData {
  query: string;
}

interface PlacePickerProps {
  isOpen: boolean;
  onClose?: () => void;
  onSelect?: (placeId: string) => void;
  suggestions?: SuggestionPlace[];
}

export default function SearchPlaceModal({
  isOpen,
  onClose,
  onSelect,
  suggestions,
}: PlacePickerProps) {
  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<SearchPlaceFormData>();

  useEffect(() => {
    setValue('query', '');
  }, [isOpen, setValue]);

  const [query, setQuery] = useState('');
  const { data, fetchNextPage, isFetching } = useSearchPlaceQuery(query);

  const handleNextPage = (isIntersecting: boolean) => {
    if (isIntersecting && !isFetching && query !== '') {
      fetchNextPage();
    }
  };
  const { ref } = useIntersection({
    initialState: false,
    onChange: handleNextPage,
  });

  const onSubmit = handleSubmit((data) => {
    setQuery(data.query);
  });

  const handleSelect = useCallback(
    (...args: Parameters<NonNullable<typeof onSelect>>) => {
      onClose?.();
      onSelect?.(...args);
    },
    [onClose, onSelect]
  );

  return (
    <Drawer isOpen={isOpen} onClose={onClose}>
      <div className={s.container}>
        <form onSubmit={onSubmit} className={s.form}>
          <SearchInput
            id="place"
            isError={!!errors.query}
            {...register('query', { required: true })}
          />
        </form>
        <div className={s.result__container}>
          <SearchPlaceResult
            pages={data?.pages}
            suggestions={suggestions}
            onSelect={handleSelect}
          />
          {data?.pages.length && data.pages.at(-1)?.length ? (
            <>
              <button
                className={s.result__footer}
                onClick={() => fetchNextPage()}
              >
                더보기
              </button>
              <div
                ref={ref}
                key={data.pages.length}
                aria-hidden
                style={{ height: 1 }}
              />
            </>
          ) : null}
          {isFetching && data?.pages.length && (
            <div className={s.result__footer}>Loading...</div>
          )}
        </div>
      </div>
    </Drawer>
  );
}

function SearchPlaceResult({
  pages,
  suggestions,
  onSelect,
}: {
  pages?: Place[][];
  suggestions?: PlacePickerProps['suggestions'];
  onSelect?: PlacePickerProps['onSelect'];
}) {
  const result = useMemo(() => pages?.flat(), [pages]);

  return (
    <List className={s.result}>
      {suggestions?.length ? (
        <>
          <List.Subheader>
            <p className={s.description}>장소 제안</p>
          </List.Subheader>
          {suggestions.map((place) => (
            <List.Item
              key={place.id}
              onClick={() => onSelect?.(place.id.toString())}
            >
              <PlaceItem place={place} />
            </List.Item>
          ))}
          <div className={s.divider} />
        </>
      ) : null}
      <List.Subheader>
        <p className={s.description}>검색 결과</p>
      </List.Subheader>
      {!result?.length ? (
        <li className={s.noResult}>
          <p>검색 결과가 없습니다</p>
          <Link href="/write/place">장소 추가하기</Link>
        </li>
      ) : null}
      {result?.map((place) => (
        <List.Item
          key={place.id}
          onClick={() => onSelect?.(place.id.toString())}
        >
          <PlaceItem key={place.id} place={place} />
        </List.Item>
      ))}
    </List>
  );
}

function PlaceItem({
  place,
}: {
  place: Pick<Place, 'id' | 'name' | 'address' | 'category'>;
}) {
  return (
    <div className={s.place}>
      <div className={s.place__title}>
        <span className={s.place__name}>{place.name}</span>
        <span className={s.place__category}>
          {getCategoryLabel(place.category)}
        </span>
      </div>
      <span className={s.place__address}>{place.address}</span>
    </div>
  );
}
