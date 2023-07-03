import { useCollectionsQuery } from '@/hooks/query';
import Header from '@/components/Layout/Header';
import CollectionsList from '@/components/CollectionsList';
import MetaData from '@/components/MetaData';

export default function Collections() {
  const { data, isLoading, isError } = useCollectionsQuery();

  return (
    <>
      <MetaData title="컬렉션" />
      <Header />
      {isLoading || isError ? null : <CollectionsList collections={data} />}
    </>
  );
}
