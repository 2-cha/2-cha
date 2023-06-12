import PlaceList from '@/components/PlaceList';
import MetaData from '@/components/MetaData';
import Header from '@/components/Layout/Header';
import FilterMenu from '@/components/PlaceList/FilterMenu';
import Skeleton from '@/components/Skeleton';
import { usePlacesQuery } from '@/hooks/query/usePlaces';
import { useIntersection } from '@/hooks/useIntersection';
import { useCallback } from 'react';
import { useRecoilValue } from 'recoil';
import { placesQueryParamsState } from '@/atoms/placesQueryParams';

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
      <MetaData title="Places" />
      <Header />
      <FilterMenu />
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
