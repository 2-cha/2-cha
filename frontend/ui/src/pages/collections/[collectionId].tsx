import { useRouter } from 'next/router';

import CollectionInfo from '@/components/CollectionInfo';
import MetaData from '@/components/MetaData';
import { useCollectionQuery } from '@/hooks/query';
import { useCollectionRecommendations } from '@/hooks/query/useCollection';

export default function CollectionInfoPage() {
  const { query } = useRouter();
  const {
    data: collectionInfo,
    isLoading,
    isError,
  } = useCollectionQuery(query.collectionId);

  const {
    data: collectionRecommendations,
    isError: wasError,
    isLoading: wasLoading,
  } = useCollectionRecommendations(query.collectionId);

  return (
    <>
      <MetaData title={collectionInfo?.title} />
      {isLoading || isError ? null : (
        <CollectionInfo
          collectionInfo={collectionInfo}
          collectionRecommendations={collectionRecommendations}
        />
      )}
    </>
  );
}
