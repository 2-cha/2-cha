import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';

import { useAddressQuery } from '@/hooks/query';
import Drawer from '@/components/Layout/Drawer';
import { type Address } from '@/pages/api/address';
import SearchInput from '@/components/SearchInput';

import s from './SearchAddressModal.module.scss';

interface SearchAddressModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSelect?: (address: Address) => void;
  type?: Address['address_type'];
}

interface SearchAddressForm {
  address: string;
}

export default function SearchAddressModal({
  isOpen,
  onClose,
  onSelect,
  type,
}: SearchAddressModalProps) {
  const [query, setQuery] = useState('');
  const { data: addresses } = useAddressQuery(query, type);

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

  const handleSelect = (address: Address) => {
    onSelect?.(address);
    onClose();
  };

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
          <AddressList addresses={addresses} onSelect={handleSelect} />
        )}
      </div>
    </Drawer>
  );
}

interface AddressListProps {
  addresses: Address[];
  onSelect?: (address: Address) => void;
}

function AddressList({ addresses, onSelect }: AddressListProps) {
  return (
    <div className={s.addressList__container}>
      <ul className={s.addressList}>
        {addresses.length === 0 && (
          <li className={s.noResult}>검색 결과가 없습니다.</li>
        )}
        {addresses.map((address) => (
          <li key={address.address_name}>
            <button onClick={() => onSelect?.(address)}>
              {address.address_name}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}