import { Collection } from '@/types/collection';
import CollectionsElement from './CollectionsElement';

import s from './CollectionsList.module.scss';

interface ListProps {
  collections: Collection[];
}

export default function CollectionsList({ collections }: ListProps) {
  return (
    <div className={s.wrapper}>
      <div className={s.wrapper__header}>
        <h1>회원님을 위한 컬렉션을 준비했어요.</h1>
      </div>
      <ul className={s.wrapper__inner}>
        {collections.map((collection) => (
          <CollectionsElement key={collection.id} collection={collection} />
        ))}
      </ul>
    </div>
  );
}
