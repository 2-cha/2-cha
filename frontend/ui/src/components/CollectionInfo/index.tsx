import Image from 'next/image';
import cn from 'classnames';
import { useRouter } from 'next/router';
import { Swiper, SwiperSlide } from 'swiper/react';

import { useAuth } from '@/hooks';
import { Collection } from '@/types/collection';
import ReviewCard from './ReviewCard';
import {
  BookmarkToggleButton,
  FollowToggleButton,
  LikeToggleButton,
  ShareButton,
} from '../Buttons';
import { ArrowIcon, TrashIcon } from '../Icons';

import 'swiper/css';
import s from './CollectionInfo.module.scss';
import DeleteButton from '../Buttons/DeleteButton';

interface Props {
  collectionInfo: Collection;
}

export default function CollectionInfo({ collectionInfo }: Props) {
  const { member, reviews } = collectionInfo;
  const { user } = useAuth();
  const memberId = user?.sub;
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
      <Swiper scrollbar className={s.carousel} wrapperClass={s.wrapper}>
        {reviews.map((review) => (
          <SwiperSlide key={`review-${review.id}`}>
            <ReviewCard review={review} key={`review-${review.id}`} />
          </SwiperSlide>
        ))}
      </Swiper>
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
              isBookmarked={collectionInfo.bookmark_status.is_bookmarked}
              bookmarkCount={collectionInfo.bookmark_status.count}
              className={s.metadata__buttons__bookmark}
            />
            {member.id === Number(memberId) && (
              <DeleteButton itemType="collections" itemId={collectionInfo.id} />
            )}
          </div>
        </div>
        <h1 className={s.metadata__title}>{collectionInfo.title}</h1>
      </div>
    </div>
  );
}
