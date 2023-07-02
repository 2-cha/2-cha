import { Collection } from '@/types/collection';

import 'swiper/scss';
import s from './CollectionsList.module.scss';
import CollectionsElement from '@/components/CollectionsList/CollectionsElement';

interface ListProps {
  collections: Collection[];
}

export default function CollectionsList({ collections }: ListProps) {
  return (
    <div className={s.wrapper}>
      <div className={s.wrapper__header}>
        <h1>회원님을 위한 컬렉션을 준비했어요.</h1>
      </div>
      <div className={s.wrapper__inner}>
        {collections.map((collection, index) => (
          <CollectionsElement
            key={collection.id}
            collection={collection}
            style={{ transform: `translateY(${index}%` }}
          />
        ))}
        <div className={s.wrapper__footer}>
          <h1>컬렉션 어쩌구 저쩌구</h1>
        </div>
      </div>
    </div>
  );
}
