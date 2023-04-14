import { useCurrentLocation } from '@/hooks/location';
import { useRegion } from '@/hooks/region';
import s from './Header.module.scss';

export default function Header() {
  const { refresh, isLoading, isError } = useCurrentLocation();
  const { data: region } = useRegion();

  if (isError) {
    // TODO: 에러 처리
  }

  return (
    <header className={s.header}>
      <button onClick={() => refresh()} disabled={isLoading}>
        {/* TODO: replace to icon */}
        refresh
      </button>
      <h2 className={s.header__title}>{region?.region_3depth_name}</h2>
    </header>
  );
}
