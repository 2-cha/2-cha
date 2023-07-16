import { Fragment, useCallback, useEffect, useMemo, useState } from 'react';
import { useForm } from 'react-hook-form';

import { useSearchPlaceQuery, useKeywordQuery } from '@/hooks/query';
import { useAddPlaceMutation } from '@/hooks/mutation';
import { getCategoryLabel } from '@/lib/placeUtil';
import Drawer from '@/components/Layout/Drawer';
import SearchInput from '@/components/SearchInput';
import List from '@/components/Layout/List';
import Spinner from '@/components/Spinner';
import type { Place, SuggestionPlace } from '@/types';
import { type Place as KakaoPlace } from '@/pages/api/keyword';

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
  const places = useMemo(() => data?.pages?.flat(), [data?.pages]);

  // 쿼리의 결과가 없거나 정확하게 일치하는 장소가 없을 경우
  const showNewPlaces =
    query != '' &&
    (!places?.length || !places.find((place) => place.name === query));

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
            places={places}
            suggestions={suggestions}
            onSelect={handleSelect}
          />
          {data?.pages.length && data.pages.at(-1)?.length ? (
            <>
              <button
                className={s.result__footer}
                onClick={() => fetchNextPage()}
                disabled={isFetching}
              >
                더보기
              </button>
            </>
          ) : null}
          {isFetching && data?.pages.length && (
            <div className={s.result__footer}>
              <Spinner />
            </div>
          )}
          {showNewPlaces && (
            <>
              <div className={s.divider} />
              <NewPlaceQueryResult query={query} onSelect={onSelect} />
            </>
          )}
        </div>
      </div>
    </Drawer>
  );
}

function SearchPlaceResult({
  places,
  suggestions,
  onSelect,
}: {
  places?: Place[];
  suggestions?: PlacePickerProps['suggestions'];
  onSelect?: PlacePickerProps['onSelect'];
}) {
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
      {places ? (
        <>
          {places.map((place) => (
            <List.Item
              key={place.id}
              onClick={() => onSelect?.(place.id.toString())}
            >
              <PlaceItem key={place.id} place={place} />
            </List.Item>
          ))}
        </>
      ) : (
        <li className={s.noResult}>
          <p>검색 결과가 없습니다</p>
        </li>
      )}
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

function parseCategory(category: string) {
  return category.split('>').at(-1);
}

function NewPlaceQueryResult({
  query,
  onSelect,
}: {
  query: string;
  onSelect?: PlacePickerProps['onSelect'];
}) {
  const { data } = useKeywordQuery(query);
  const mutation = useAddPlaceMutation();

  const handleSelect = (place: KakaoPlace) => {
    mutation.mutate(
      {
        name: place.place_name,
        category: parseCategory(place.category_name),
        address: place.road_address_name,
        lot_address: place.address_name,
        lat: place.y,
        lon: place.x,
      },
      {
        onSuccess: (result) => {
          onSelect?.(result.id);
        },
        onError: console.error,
        /* TODO: toast("장소 등록에 실패했어요") */
      }
    );
  };

  if (!data) {
    return null;
  }

  return (
    <List className={s.result}>
      <List.Subheader>
        <p className={s.description}>새로운 장소</p>
      </List.Subheader>
      {data?.pages.map((response, idx) => (
        <Fragment key={idx}>
          {response.documents.map((place) => (
            <List.Item key={place.id} onClick={() => handleSelect(place)}>
              <div className={s.place}>
                <div className={s.place__title}>
                  <span className={s.place__name}>{place.place_name}</span>
                  <span className={s.place__category}>
                    {parseCategory(place.category_name)}
                  </span>
                </div>
                <span className={s.place__address}>
                  {place.road_address_name}
                </span>
              </div>
            </List.Item>
          ))}
        </Fragment>
      ))}
    </List>
  );
}
