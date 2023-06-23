import CollectionInfo from '@/components/CollectionInfo';
import MetaData from '@/components/MetaData';
import { useCollectionQuery } from '@/hooks/query/useCollection';
import { Collection } from '@/types/collection';
import { useRouter } from 'next/router';

export default function CollectionInfoPage() {
  const { query } = useRouter();
  const {
    data: collectionInfo,
    isLoading,
    isError,
  } = useCollectionQuery(query.collectionId);

  return (
    <>
      <MetaData title={collectionInfo?.title} />
      {isLoading || isError ? null : (
        <CollectionInfo collectionInfo={collectionInfo} />
      )}
    </>
  );
}
