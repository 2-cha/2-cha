import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useSetRecoilState } from 'recoil';

import { useAddressQuery } from '@/hooks/query';
import Drawer from '@/components/Layout/Drawer';
import { locationState } from '@/atoms/location';
import { type Address } from '@/pages/api/address';
import SearchInput from '@/components/SearchInput';

import s from './SearchAddressModal.module.scss';

interface SearchAddressModalProps {
  isOpen: boolean;
  onClose: () => void;
}

interface SearchAddressForm {
  address: string;
}

export default function SearchAddressModal({
  isOpen,
  onClose,
}: SearchAddressModalProps) {
  const [query, setQuery] = useState('');
  const { data: addresses } = useAddressQuery(query);

  const {
    handleSubmit,
    register,
    setValue,
    formState: { errors },
  } = useForm<SearchAddressForm>();
  const onSubmit = handleSubmit((data) => {
    setQuery(data.address);
  });

  useEffect(() => {
    setValue('address', '');
  }, [isOpen, setValue]);

  return (
    <Drawer isOpen={isOpen} onClose={onClose}>
      <div className={s.container}>
        <form className={s.searchForm} onSubmit={onSubmit}>
          <SearchInput
            id="address"
            placeholder="주소를 입력하세요"
            isError={!!errors.address}
            {...register('address', { required: true })}
          />
        </form>
        {addresses && (
          <AddressList addresses={addresses} onSelected={onClose} />
        )}
      </div>
    </Drawer>
  );
}

interface AddressListProps {
  addresses: Address[];
  onSelected?: () => void;
}

function AddressList({ addresses, onSelected }: AddressListProps) {
  const setLocationState = useSetRecoilState(locationState);

  return (
    <div className={s.addressList__container}>
      <ul className={s.addressList}>
        {addresses.length === 0 && (
          <li className={s.noResult}>검색 결과가 없습니다.</li>
        )}
        {addresses.map((address) => (
          <li key={address.address_name}>
            <button
              onClick={() => {
                onSelected && onSelected();
                setLocationState({
                  lat: Number(address.y),
                  lon: Number(address.x),
                });
              }}
            >
              {address.address_name}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
