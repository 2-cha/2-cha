import { FormProvider, useForm, useFormContext } from 'react-hook-form';
import { useRouter } from 'next/router';
import cn from 'classnames';

import KeywordSearchModal from '@/components/KeywordSearchModal';
import SearchAddressModal from '@/components/SearchAddressModal';
import { useModal } from '@/hooks';
import { getCategoryLabel } from '@/lib/placeUtil';
import { useAddPlaceMutation } from '@/hooks/mutation';
import { type Place } from '@/pages/api/keyword';
import { type Address } from '@/pages/api/address';

import s from './AddPlace.module.scss';

interface AddPlaceFormData {
  name: string;
  address: string;
  lot_address?: string;
  category: string;
  lat: string;
  lon: string;
}

export default function AddPlace() {
  const { isOpen, onOpen, onClose } = useModal();
  const method = useForm<AddPlaceFormData>();

  const handleSelectPlace = (place: Place) => {
    method.setValue('name', place.place_name);
    method.setValue('address', place.road_address_name);
    method.setValue('lot_address', place.address_name);
    method.setValue('lat', place.y);
    method.setValue('lon', place.x);
  };

  return (
    <FormProvider {...method}>
      <div className={s.container}>
        <button className={s.button} onClick={onOpen}>
          장소 검색
        </button>
        <AddPlaceForm />
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

function AddPlaceForm() {
  const { register, handleSubmit, setValue, watch } =
    useFormContext<AddPlaceFormData>();
  const router = useRouter();
  const mutation = useAddPlaceMutation();

  const { isOpen, onOpen, onClose } = useModal();
  const handleSelect = (address: Address) => {
    setValue('address', address.road_address.address_name);
    setValue('lot_address', address.address.address_name);
    setValue('lat', address.road_address.y);
    setValue('lon', address.road_address.x);
  };

  const address = watch('address');

  const onSubmit = handleSubmit((data) => {
    mutation.mutate(data, {
      onSuccess: (result) => {
        router.push(`/write?placeId=${result.id}`);
      },
      onError: console.error,
    });
  }, console.error);

  return (
    <>
      <form onSubmit={onSubmit} className={s.form}>
        <div>
          <label htmlFor="name">장소</label>
          <input type="text" {...register('name', { required: true })} />
        </div>
        <div>
          <label htmlFor="address">주소</label>
          <input
            type="text"
            hidden
            {...register('address', { required: true })}
          />
          <button className={s.fakeInput} type="button" onClick={onOpen}>
            {address || <span>주소 검색</span>}
          </button>
        </div>
        <div>
          <label htmlFor="category">카테고리</label>
          <select {...register('category', { required: true })}>
            <option value="">선택</option>
            {category.map((item) => (
              <option key={item} value={item}>
                {getCategoryLabel(item)}
              </option>
            ))}
          </select>
        </div>
        <button type="submit" className={cn(s.button, s.submit)}>
          등록
        </button>
      </form>
      <SearchAddressModal
        isOpen={isOpen}
        onClose={onClose}
        onSelect={handleSelect}
        type="ROAD_ADDR"
      />
    </>
  );
}
