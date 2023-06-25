import { useKeywordQuery } from '@/hooks/query/useKeyword';
import { useState } from 'react';
import { useForm } from 'react-hook-form';

interface RegisterPlaceFormData {
  name: string;
  address: string;
  category: string;
}

export default function RegisterPlaceForm() {
  const { register, handleSubmit } = useForm<RegisterPlaceFormData>();

  const onSubmit = handleSubmit(
    (data) => {},
    (errors) => {}
  );

  return (
    <form onSubmit={onSubmit}>
      <input type="text" {...register('name')} />
      <input type="text" {...register('address')} />
      {/* TODO: select로 변경 */}
      <input {...register('category')} />
      <button type="submit">등록</button>
    </form>
  );
}

function KeywordSearchModal({ query }: { query?: string }) {
  const [keyword] = useState(query ?? '');
  const { data } = useKeywordQuery(keyword);

  return (
    <ul>
      {data?.map((place) => (
        <li key={place.id}>
          <button>
            <p>{place.place_name}</p>
            <p>{place.road_address_name}</p>
          </button>
        </li>
      ))}
    </ul>
  );
}
