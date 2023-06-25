import { useCollectionsQuery } from '@/hooks/query';
import Link from 'next/link';

import { Collection } from '@/types/collection';

export default function Collections() {
  const { data, isLoading, isError } = useCollectionsQuery();
  return (
    <div>
      {isLoading || isError ? null : (
        <>
          {data.map((collection: Collection) => (
            <Link href={`/collections/${collection.id}`} key={collection.id}>
              {collection.title}
            </Link>
          ))}
        </>
      )}
    </div>
  );
}
