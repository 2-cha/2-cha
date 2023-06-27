import Link from 'next/link';

import { useAuth } from '@/hooks';
import { useCollectionsQuery } from '@/hooks/query';
import { Collection } from '@/types/collection';
import Header from '@/components/Layout/Header';
import { CollectionsHeader } from '@/components/CollectionsList';

export default function Collections() {
  const { data, isLoading, isError } = useCollectionsQuery();

  return (
    <>
      <Header />
      <CollectionsHeader />
      {isLoading || isError ? null : (
        <>
          {data.map((collection: Collection) => (
            <Link href={`/collections/${collection.id}`} key={collection.id}>
              {collection.title}
            </Link>
          ))}
        </>
      )}
    </>
  );
}
