import NavStackHeader from '@/components/Layout/NavStackHeader';
import MetaData from '@/components/MetaData';
import PlaceInfo from '@/components/PlaceInfo';
import { useIntersection } from '@/hooks/intersection';
import { usePlaceQuery } from '@/hooks/place';
import { useRouter } from 'next/router';

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
      {isLoading || isError ? null : (
        <PlaceInfo placeInfo={placeInfo} ref={ref} />
      )}
      {/* TODO: 지도, 리뷰 */}
    </>
  );
}
