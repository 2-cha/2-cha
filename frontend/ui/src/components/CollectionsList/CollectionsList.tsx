import { Collection } from '@/types/collection';
import CollectionsElement from './CollectionsElement';

import s from './CollectionsList.module.scss';

interface ListProps {
  collections: Collection[];
}

export default function CollectionsList({ collections }: ListProps) {
  // const { user } = useAuth();

  return (
    <div>
      <div className={s.header}>
        {/* <h1>{user?.name}님을 위한 컬렉션.</h1> */}
        <h1>유저님을 위한 컬렉션을 준비했어요.</h1>
      </div>
      <ul className={s.wrapper}>
        {collections.map((collection) => (
          <CollectionsElement key={collection.id} collection={collection} />
        ))}
      </ul>
    </div>
  );
}
