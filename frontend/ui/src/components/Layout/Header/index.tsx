import { useCurrentLocation } from '@/hooks/location';
import { useRegionQuery } from '@/hooks/region';
import LocationIcon from '@/components/Icons/LocationIcon';
import s from './Header.module.scss';

export default function Header() {
  const { refresh, location, isLoading, isError } = useCurrentLocation();
  const { data: region } = useRegionQuery(location);

  if (isError) {
    // TODO: 에러 처리
  }

  return (
    <header className={s.header}>
      <button
        onClick={() => refresh()}
        disabled={isLoading}
        className={s.header__button}
      >
        <LocationIcon />
      </button>
      <p className={s.header__title}>{region?.region_3depth_name}</p>
    </header>
  );
}
