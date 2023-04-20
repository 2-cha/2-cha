import PlaceList from '@/components/PlaceList';
import MetaData from '@/components/MetaData';
import Header from '@/components/Layout/Header';
import { usePlacesQuery } from '@/hooks/places';
import { useIntersection } from '@/hooks/intersection';
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
      {/* TODO: fallback 컴포넌트 만들기 */}
      {isLoading ? (
        <div>loading</div>
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
