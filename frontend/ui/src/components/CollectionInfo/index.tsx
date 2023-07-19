import cn from 'classnames';
import { useRouter } from 'next/router';
import { Swiper, SwiperSlide } from 'swiper/react';

import { useAuth } from '@/hooks';
import { Collection } from '@/types/collection';
import ReviewCard from './ReviewCard';
import {
  BookmarkToggleButton,
  DeleteButton,
  FollowToggleButton,
  LikeToggleButton,
  ProfileButton,
  ShareButton,
} from '../Buttons';
import { ArrowIcon } from '../Icons';

import 'swiper/css';
import s from './CollectionInfo.module.scss';

interface Props {
  collectionInfo: Collection;
}

export default function CollectionInfo({ collectionInfo }: Props) {
  const { member, reviews } = collectionInfo;
  const { user } = useAuth();
  const memberId = user?.sub;
  const router = useRouter();

  const isMine = member.id === Number(memberId);

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
      <Swiper
        scrollbar
        className={s.carousel}
        wrapperClass={s.wrapper}
        autoHeight={false}
      >
        {reviews.map((review) => (
          <SwiperSlide key={`review-${review.id}`}>
            <ReviewCard review={review} key={`review-${review.id}`} />
          </SwiperSlide>
        ))}
      </Swiper>
      <div className={s.metadata}>
        <div className={s.metadata__top}>
          <ProfileButton
            member={member}
            imageSize={100}
            className={s.metadata__profile}
          />
          <div className={s.metadata__buttons}>
            {isMine ? (
              <></>
            ) : (
              <FollowToggleButton
                userId={member.id}
                className={s.metadata__buttons__follow}
              />
            )}
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
          </div>
        </div>
        <div className={s.metadata__bottom}>
          <div className={s.metadata__buttons}>
            <span className={s.metadata__title}>{collectionInfo.title}</span>
            <div className={s.metadata__buttons}>
              {isMine ? (
                <DeleteButton
                  itemType="collections"
                  itemId={collectionInfo.id}
                  className={s.metadata__buttons__follow}
                />
              ) : (
                <></>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
