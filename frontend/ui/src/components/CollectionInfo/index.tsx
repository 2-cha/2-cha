import cn from 'classnames';
import { useRouter } from 'next/router';
import { Swiper, SwiperRef, SwiperSlide } from 'swiper/react';
import { EffectCards, Navigation } from 'swiper';

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
import { ArrowIcon, SimpleArrowIcon } from '../Icons';

import 'swiper/css';
import 'swiper/css/effect-cards';
import 'swiper/css/navigation';
import s from './CollectionInfo.module.scss';

import React, { useEffect, useRef } from 'react';
import CollectionsElement from '@/components/CollectionsList/CollectionsElement';

interface Props {
  collectionInfo: Collection;
  collectionRecommendations: Collection[];
}

export default function CollectionInfo({
  collectionInfo,
  collectionRecommendations,
}: Props) {
  const { member, reviews } = collectionInfo;
  const { user } = useAuth();
  const memberId = user?.sub;
  const router = useRouter();

  const isMine = member.id === Number(memberId);
  const swiperRef = useRef<SwiperRef>(null);
  useEffect(() => {
    swiperRef?.current?.swiper?.slideTo(0);
    console.log(swiperRef?.current?.swiper);
  }, [router.asPath]);

  function handleClickBack() {
    router.back();
  }

  return (
    <div className={s.root}>
      <Swiper
        ref={swiperRef}
        className={s.root}
        direction={'vertical'}
        autoHeight={true}
        modules={[Navigation]}
        navigation={{ enabled: true, nextEl: '.next' }}
      >
        <SwiperSlide className={s.top}>
          <nav className={s.top__nav}>
            <button
              type="button"
              className={s.top__button}
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
                <span className={s.metadata__title}>
                  {collectionInfo.title}
                </span>
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
            <div className={s.metadata__footer}>
              <div className={s.bounce}>
                <button
                  className={'next'}
                  style={{
                    transform: 'rotate(180deg)',
                    color: 'var(--border-color)',
                    background: 'none',
                  }}
                >
                  <SimpleArrowIcon />
                </button>
              </div>
            </div>
          </div>
        </SwiperSlide>
        <SwiperSlide className={s.bottom}>
          <p className={s.bottom__header}>비슷한 컬렉션들이 있어요.</p>
          <div className={s.bottom__recommendation_container}>
            <Swiper
              scrollbar
              modules={[EffectCards]}
              effect={'cards'}
              cardsEffect={{ slideShadows: false }}
              loop={true}
              autoHeight={false}
              wrapperClass={s.bottom__recommendation_wrapper}
            >
              {collectionRecommendations?.map((c) => (
                <SwiperSlide
                  key={`collection-recommendation-${c.id}`}
                  className={s.bottom__recommendation}
                >
                  <CollectionsElement
                    collection={c}
                    key={`collection-recommendation-${c.id}`}
                    style={{}}
                  />
                </SwiperSlide>
              ))}
            </Swiper>
          </div>
        </SwiperSlide>
      </Swiper>
    </div>
  );
}
