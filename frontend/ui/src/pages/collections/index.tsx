import { useCollectionsQuery } from '@/hooks/query';
import Header from '@/components/Layout/Header';
import CollectionsList from '@/components/CollectionsList';

export default function Collections() {
  const { data, isLoading, isError } = useCollectionsQuery();

  return (
    <>
      <Header />
      {isLoading || isError ? null : <CollectionsList collections={data} />}
    </>
  );
}
