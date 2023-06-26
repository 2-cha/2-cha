import { useState } from 'react';

import { useKeywordQuery } from '@/hooks/query/useKeyword';
import type { Place } from '@/pages/api/keyword';
import Drawer from '../Layout/Drawer';
import SearchInput from '../SearchInput';

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
  const { data } = useKeywordQuery(keyword);

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
      <form onSubmit={handleSubmit}>
        <SearchInput name="query" />
      </form>
      <ul>
        {data?.map((place) => (
          <li key={place.id}>
            <button onClick={() => handleSelect(place)}>
              <p>{place.place_name}</p>
              <p>{place.road_address_name}</p>
            </button>
          </li>
        ))}
      </ul>
    </Drawer>
  );
}
