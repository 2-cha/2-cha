import PlaceList from '@/components/PlaceList';
import MetaData from '@/components/MetaData';
import Header from '@/components/Layout/Header';
import Skeleton from '@/components/Skeleton';
import { usePlacesQuery } from '@/hooks/query/usePlaces';
import { useIntersection } from '@/hooks/useIntersection';
import { useCallback } from 'react';

export default function Places() {
  const { data, isLoading, isError, isFetching, fetchNextPage } =
    usePlacesQuery();
  const handleNextPage = useCallback(
    (isIntersecting: boolean) => isIntersecting && fetchNextPage(),
    [fetchNextPage]
  );
  const { ref } = useIntersection({ onChange: handleNextPage });

  return (
    <>
      <MetaData title="Places" />
      <Header />
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
