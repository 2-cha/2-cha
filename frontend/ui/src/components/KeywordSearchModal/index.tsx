import { Fragment, useState } from 'react';

import { useKeywordQuery } from '@/hooks/query/useKeyword';
import Drawer from '@/components/Layout/Drawer';
import List from '@/components/Layout/List';
import SearchInput from '@/components/SearchInput';
import type { Place } from '@/pages/api/keyword';

import s from './KeywordSearchModal.module.scss';

interface KeywordSearchModalProps {
  query?: string;
  isOpen: boolean;
  onClose?: () => void;
  onSelect?: (place: Place) => void;
}

export default function KeywordSearchModal({
  query,
  isOpen,
  onClose,
  onSelect,
}: KeywordSearchModalProps) {
  const [keyword, setKeyword] = useState(query ?? '');
  const { data, fetchNextPage } = useKeywordQuery(keyword);

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const q = new FormData(e.currentTarget).get('query');
    if (q) {
      setKeyword(q.toString());
    }
  };

  const handleSelect = (place: Place) => {
    onSelect?.(place);
    onClose?.();
  };

  return (
    <Drawer isOpen={isOpen} onClose={onClose} header="장소 검색">
      <div className={s.container}>
        <form onSubmit={handleSubmit}>
          <SearchInput name="query" />
        </form>
        <List className={s.list}>
          {data?.pages.at(-1)?.meta.total_count === 0 ? (
            <span className={s.noResult}>검색 결과가 없습니다.</span>
          ) : null}
          {data?.pages.map((page, idx) => (
            <Fragment key={idx}>
              {page.documents.map((place) => (
                <List.Item key={place.id} onClick={() => handleSelect(place)}>
                  <div className={s.item}>
                    <p className={s.item__title}>{place.place_name}</p>
                    <p className={s.item__description}>
                      {place.road_address_name}
                    </p>
                  </div>
                </List.Item>
              ))}
            </Fragment>
          ))}
          {data?.pages.length && !data.pages.at(-1)?.meta.is_end ? (
            <List.Item onClick={fetchNextPage}>더보기</List.Item>
          ) : null}
        </List>
      </div>
    </Drawer>
  );
}
