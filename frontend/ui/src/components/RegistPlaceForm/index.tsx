import { FormProvider, useForm, useFormContext } from 'react-hook-form';

import KeywordSearchModal from '@/components/KeywordSearchModal';
import { useModal } from '@/hooks';
import { getCategoryLabel } from '@/lib/placeUtil';
import { type Place } from '@/pages/api/keyword';
import { useRegistPlaceMutation } from '@/hooks/mutation';
import { useRouter } from 'next/router';

interface RegisterPlaceFormData {
  name: string;
  address: string;
  lot_address?: string;
  category: string;
  lat: string;
  lon: string;
}

export default function RegisterPlaceForm() {
  const { isOpen, onOpen, onClose } = useModal();
  const method = useForm<RegisterPlaceFormData>();

  const handleSelectPlace = (place: Place) => {
    method.setValue('name', place.place_name);
    method.setValue('address', place.road_address_name);
    method.setValue('lot_address', place.address_name);
    method.setValue('lat', place.y);
    method.setValue('lon', place.x);
  };

  return (
    <FormProvider {...method}>
      <div>
        <button onClick={onOpen}>이름으로 찾아보기</button>
        <p>혹은</p>
        <p>직접 추가하기</p>
        <Form />
      </div>
      <KeywordSearchModal
        isOpen={isOpen}
        onClose={onClose}
        onSelect={handleSelectPlace}
      />
    </FormProvider>
  );
}

// TODO: constants
const category = ['COCKTAIL_BAR', 'WINE_BAR', 'BEER_BAR', 'WHISKEY_BAR'];

function Form() {
  const { register, handleSubmit } = useFormContext<RegisterPlaceFormData>();
  const router = useRouter();
  const mutate = useRegistPlaceMutation();

  const onSubmit = handleSubmit((data) => {
    mutate.mutate(data, {
      onSuccess: (data) => {
        router.push(`/write?placeId=${data.id}`);
      },
      onError: console.error,
    });
  }, console.error);

  // TODO: styles
  return (
    <form onSubmit={onSubmit}>
      <input type="text" {...register('name', { required: true })} />
      {/* TODO: 주소 검색 모달로 변경 */}
      <input type="text" {...register('address', { required: true })} />
      <select {...register('category', { required: true })}>
        <option value="">카테고리</option>
        {category.map((item) => (
          <option key={item} value={item}>
            {getCategoryLabel(item)}
          </option>
        ))}
      </select>
      <button type="submit">등록</button>
    </form>
  );
}
