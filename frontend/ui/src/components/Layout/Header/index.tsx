import { useEffect, useState } from 'react';
import { useCurrentLocation } from '@/hooks/location';
import { useRegionQuery } from '@/hooks/region';
import { useAddressQuery } from '@/hooks/address';
import { useSetRecoilState } from 'recoil';
import { locationState } from '@/atoms/location';
import { type Address } from '@/pages/api/address';
import LocationIcon from '@/components/Icons/LocationIcon';
import s from './Header.module.scss';

export default function Header() {
  const { location, isError } = useCurrentLocation();
  const [isFormOpen, setFormOpen] = useState(false);

  useEffect(() => {
    if (!location && isError) {
      setFormOpen(true);
    }
  }, [location, isError]);

  // 지역 이름
  const { data: region } = useRegionQuery(location);
  let title = region?.region_3depth_name || '';
  if (!region && isError) {
    title = '-';
  }

  // 주소 검색
  const [query, setQuery] = useState('');
  const { data: addresses } = useAddressQuery(query);

  return (
    <div className={s.root}>
      <header className={s.header}>
        <button
          onClick={() => setFormOpen((prev) => !prev)}
          className={s.header__button}
        >
          <LocationIcon />
        </button>
        {isFormOpen ? (
          <SearchAddressForm onSubmit={(address) => setQuery(address)} />
        ) : (
          <p className={s.header__title}>{title}</p>
        )}
      </header>
      {addresses && isFormOpen ? (
        <AddressList
          addresses={addresses}
          onSelected={() => setFormOpen(false)}
        />
      ) : null}
    </div>
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
    <ul className={s.addressList}>
      {addresses.length === 0 && <li>검색 결과가 없습니다.</li>}
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
  );
}
