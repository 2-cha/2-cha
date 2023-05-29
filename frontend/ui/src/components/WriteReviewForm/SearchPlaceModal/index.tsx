import { useCallback, useEffect, useMemo, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useSearchPlaceQuery } from '@/hooks/query/useSearchPlaceQuery';
import { useIntersection } from '@/hooks/useIntersection';
import { getCategoryLabel } from '@/lib/placeUtil';
import SearchIcon from '@/components/Icons/SearchIcon';
import Drawer from '@/components/Layout/Drawer';
import type { Place } from '@/types';
import s from './SearchPlaceModal.module.scss';

interface SearchPlaceFormData {
  query: string;
}

interface PlacePickerProps {
  isOpen: boolean;
  onClose?: () => void;
  onSelect?: (placeId: string) => void;
  suggest?: Place[];
}

export default function SearchPlaceModal({
  isOpen,
  onClose,
  onSelect,
  suggest,
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
          <input
            id="place"
            type="search"
            className={s.input}
            {...register('query')}
          />
          <button type="submit" className={s.submit}>
            <SearchIcon />
          </button>
        </form>
        {errors.query && <span>검색어를 입력해주세요</span>}
        <div className={s.result__container}>
          <SearchPlaceResult
            pages={data?.pages}
            suggest={suggest}
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
  suggest,
  onSelect,
}: {
  pages?: Place[][];
  suggest?: Place[];
  onSelect?: (placeId: string) => void;
}) {
  const result = useMemo(() => pages?.flat(), [pages]);

  return (
    <ul className={s.result}>
      {!suggest?.length && !result?.length ? (
        <li className={s.noResult}>검색 결과가 없습니다</li>
      ) : null}
      {suggest?.length ? (
        <>
          <li className={s.result__item}>장소 제안</li>
          {suggest.map((place) => (
            <li
              key={place.id}
              className={s.result__item}
              onClick={() => onSelect?.(place.id.toString())}
            >
              <PlaceItem place={place} />
            </li>
          ))}
          <div className={s.divider} />
        </>
      ) : null}
      {result?.map((place) => (
        <li
          key={place.id}
          className={s.result__item}
          onClick={() => onSelect?.(place.id.toString())}
        >
          <PlaceItem key={place.id} place={place} />
        </li>
      ))}
    </ul>
  );
}

function PlaceItem({ place }: { place: Place }) {
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
