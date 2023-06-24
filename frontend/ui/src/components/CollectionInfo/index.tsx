import Image from 'next/image';

import { PlusSquareIcon } from '@/components/Icons';
import { Collection } from '@/types/collection';
import ReviewCard from './ReviewCard';
import { BookmarkToggleButton, LikeToggleButton } from '../Buttons';

import s from './CollectionInfo.module.scss';

interface Props {
  collectionInfo: Collection;
}

export default function CollectionInfo({ collectionInfo }: Props) {
  const { member, reviews } = collectionInfo;
  console.log(collectionInfo);
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
            <LikeToggleButton
              itemType="collections"
              itemId={collectionInfo.id}
              isLiked={collectionInfo.like_status.is_liked}
              nOfLikes={collectionInfo.like_status.count}
              className={s.metadata__buttons__bookmark}
            />
            <BookmarkToggleButton
              itemType="collection"
              itemId={collectionInfo.id}
              className={s.metadata__buttons__bookmark}
              nOfBookmarks={0}
            />
          </div>
        </div>
        <h1 className={s.metadata__title}>{collectionInfo.title}</h1>
      </div>
    </div>
  );
}
