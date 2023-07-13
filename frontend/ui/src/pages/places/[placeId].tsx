import { useRouter } from 'next/router';

import NavStackHeader from '@/components/Layout/NavStackHeader';
import MetaData from '@/components/MetaData';
import PlaceInfo from '@/components/PlaceInfo';
import NotFound from '@/components/NotFound';
import { useIntersection } from '@/hooks';
import { usePlaceQuery } from '@/hooks/query';

export default function PlaceInfoPage() {
  const { query } = useRouter();
  const { data: placeInfo, isLoading, isError } = usePlaceQuery(query.placeId);
  const { ref, isIntersecting } = useIntersection({ initialState: true });

  return (
    <>
      <MetaData title={placeInfo?.name} />
      <NavStackHeader hideTitle={isIntersecting}>
        {placeInfo?.name}
      </NavStackHeader>
      {isError ? (
        <NotFound />
      ) : isLoading ? null : (
        <PlaceInfo placeInfo={placeInfo} ref={ref} />
      )}
    </>
  );
}
