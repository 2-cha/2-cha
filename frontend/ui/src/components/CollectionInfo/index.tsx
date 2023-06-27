import Image from 'next/image';
import cn from 'classnames';
import { useRouter } from 'next/router';

import { Collection } from '@/types/collection';
import ReviewCard from './ReviewCard';
import {
  BookmarkToggleButton,
  FollowToggleButton,
  LikeToggleButton,
  ShareButton,
} from '../Buttons';
import { ArrowIcon } from '../Icons';

import s from './CollectionInfo.module.scss';

interface Props {
  collectionInfo: Collection;
}

export default function CollectionInfo({ collectionInfo }: Props) {
  const { member, reviews } = collectionInfo;
  const router = useRouter();

  function handleClickBack() {
    router.back();
  }

  return (
    <div className={s.root}>
      <nav className={s.root__nav}>
        <button
          type="button"
          className={s.root__button}
          onClick={handleClickBack}
        >
          <ArrowIcon />
        </button>
        <ShareButton
          sharedTitle={collectionInfo.title}
          sharedUrl={`${process.env.NEXT_PUBLIC_ORIGIN}/collections/${collectionInfo.id}`}
        />
      </nav>
      <div className={s.carousel}>
        <div className={s.carousel__inner}>
          {reviews.map((review) => (
            <ReviewCard key={`review-${review.id}`} review={review} />
          ))}
        </div>
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
            <FollowToggleButton
              userId={member.id}
              className={s.metadata__buttons__follow}
            />
            <LikeToggleButton
              itemType="collections"
              itemId={collectionInfo.id}
              isLiked={collectionInfo.like_status.is_liked}
              likeCount={collectionInfo.like_status.count}
              className={cn(
                s.metadata__buttons__bookmark,
                s.metadata__buttons__follow
              )}
            />
            <BookmarkToggleButton
              itemType="collections"
              itemId={collectionInfo.id}
              className={s.metadata__buttons__bookmark}
              bookmarkCount={0}
            />
          </div>
        </div>
        <h1 className={s.metadata__title}>{collectionInfo.title}</h1>
      </div>
    </div>
  );
}
