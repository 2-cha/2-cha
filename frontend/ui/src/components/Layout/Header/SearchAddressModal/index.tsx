import Drawer from '@/components/Layout/Drawer';
import { useState } from 'react';
import { useAddressQuery } from '@/hooks/query/useAddress';
import { useSetRecoilState } from 'recoil';
import { locationState } from '@/atoms/location';
import { type Address } from '@/pages/api/address';
import s from './SearchAddressModal.module.scss';

interface SearchAddressModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export default function SearchAddressModal({
  isOpen,
  onClose,
}: SearchAddressModalProps) {
  const [query, setQuery] = useState('');
  const { data: addresses } = useAddressQuery(query);

  return (
    <Drawer isOpen={isOpen} onClose={onClose}>
      <div className={s.container}>
        <SearchAddressForm onSubmit={setQuery} />
        {addresses && (
          <AddressList addresses={addresses} onSelected={onClose} />
        )}
      </div>
    </Drawer>
  );
}

interface SearchAddressFormProps {
  onSubmit: (address: string) => void;
}

function SearchAddressForm({ onSubmit }: SearchAddressFormProps) {
  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);
    const address = formData.get('address') as string;
    onSubmit(address);
  };

  return (
    <form className={s.searchForm} onSubmit={handleSubmit}>
      <input
        className={s.searchForm__input}
        type="text"
        id="address"
        name="address"
        placeholder="주소를 입력하세요"
      />
      <input className={s.searchForm__button} type="submit" value="검색" />
    </form>
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
