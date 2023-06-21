import { Collection } from '@/types/collection';
import ReviewCard from './ReviewCard';

import s from './CollectionInfo.module.scss';
import Image from 'next/image';
import PlusSquareIcon from '../Icons/PlusSquareIcon';
import BookmarkIcon from '../Icons/BookmarkIcon';
import HeartIcon from '../Icons/HeartIcon';

interface Props {
  collectionInfo: Collection;
}

export default function CollectionInfo({ collectionInfo }: Props) {
  const { member, reviews } = collectionInfo;
  return (
    <div className={s.root}>
      <div className={s.carousel}>
        {reviews.map((review) => (
          <ReviewCard key={`review-${review.id}`} review={review} />
        ))}
      </div>
      <div className={s.metadata}>
        <div className={s.metadata__top}>
          <div className={s.metadata__user}>
            <Image
              src={member.prof_img}
              width={100}
              height={100}
              alt="collection user profile"
            />
            <h3>{member.name}</h3>
          </div>
          <div className={s.metadata__buttons}>
            <PlusSquareIcon withoutBorder />
            <HeartIcon />
            <BookmarkIcon isSingle />
          </div>
        </div>
        <h1 className={s.metadata__title}>{collectionInfo.title}</h1>
      </div>
    </div>
  );
}
