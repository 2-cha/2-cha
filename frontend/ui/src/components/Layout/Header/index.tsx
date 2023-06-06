import { useEffect } from 'react';
import { useCurrentLocation } from '@/hooks/useCurrentLocation';
import { useRegion } from '@/hooks/query/useRegion';
import { useModal } from '@/hooks/useModal';
import LocationIcon from '@/components/Icons/LocationIcon';
import SearchAddressModal from './SearchAddressModal';
import s from './Header.module.scss';

export default function Header() {
  const { location, isError } = useCurrentLocation();
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

  return (
    <div className={s.root}>
      <header className={s.header}>
        <button onClick={onOpen} className={s.header__button}>
          <LocationIcon />
          <p className={s.header__title}>{region}</p>
        </button>
      </header>
      <SearchAddressModal isOpen={isOpen} onClose={onClose} />
    </div>
  );
}
