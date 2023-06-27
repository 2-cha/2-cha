import { useEffect } from 'react';
import { useSetRecoilState } from 'recoil';

import { useCurrentLocation, useModal } from '@/hooks';
import { locationState } from '@/atoms';
import { useRegion } from '@/hooks/query';
import { LocationIcon } from '@/components/Icons';
import SearchAddressModal from '@/components/SearchAddressModal';
import { type Address } from '@/pages/api/address';

import s from './Header.module.scss';

export default function Header() {
  const { location, isError, refresh } = useCurrentLocation();
  const { isOpen, onOpen, onClose } = useModal();

  useEffect(() => {
    if (!location && isError) {
      onOpen();
    }
  }, [location, isError, onOpen]);

  // 읍면동
  let region = useRegion(location);
  if (!region && isError) {
    region = '위치를 알 수 없어요';
  }

  const setLocationState = useSetRecoilState(locationState);
  const handleSelect = (address: Address) => {
    setLocationState({
      lat: Number(address.y),
      lon: Number(address.x),
    });
  };

  return (
    <div className={s.root}>
      <header className={s.header}>
        <div className={s.location}>
          <button onClick={refresh} className={s.header__button}>
            <LocationIcon />
          </button>
          <button onClick={onOpen} className={s.header__button}>
            <p className={s.header__title}>{region}</p>
          </button>
        </div>
      </header>
      <SearchAddressModal
        isOpen={isOpen}
        onClose={onClose}
        onSelect={handleSelect}
      />
    </div>
  );
}
