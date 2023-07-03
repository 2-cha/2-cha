import { useCallback } from 'react';
import { useRecoilValue } from 'recoil';

import PlaceList from '@/components/PlaceList';
import MetaData from '@/components/MetaData';
import Header from '@/components/Layout/Header';
import PlaceListQueryParamsMenu from '@/components/PlaceList/PlaceListQueryParamsMenu';
import Skeleton from '@/components/Skeleton';
import { useIntersection } from '@/hooks';
import { usePlacesQuery } from '@/hooks/query';
import { placesQueryParamsState } from '@/atoms';

export default function Places() {
  const placesQueryParams = useRecoilValue(placesQueryParamsState);
  const { data, isLoading, isError, isFetching, fetchNextPage } =
    usePlacesQuery(placesQueryParams);
  const handleNextPage = useCallback(
    (isIntersecting: boolean) => isIntersecting && fetchNextPage(),
    [fetchNextPage]
  );
  const { ref } = useIntersection({ onChange: handleNextPage });

  return (
    <>
      <MetaData />
      <Header />
      <PlaceListQueryParamsMenu />
      {isLoading ? (
        <>
          {Array.from({ length: 3 }).map((_, i) => (
            <Skeleton.Feed key={i} />
          ))}
        </>
      ) : isError ? (
        <div>error</div>
      ) : (
        <>
          <PlaceList pages={data.pages} />
          <div ref={ref} aria-hidden style={{ height: 1 }} />
          {isFetching && <div>Loading...</div>}
        </>
      )}
    </>
  );
}
