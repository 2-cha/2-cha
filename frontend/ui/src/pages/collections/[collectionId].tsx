import { useRouter } from 'next/router';

import CollectionInfo from '@/components/CollectionInfo';
import MetaData from '@/components/MetaData';
import { useCollectionQuery } from '@/hooks/query';
import { Collection } from '@/types/collection';

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
