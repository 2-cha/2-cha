import { usePlaceReviewsQuery } from '@/hooks/query/usePlaceReviews';
import s from './PlaceReviewsMenu.module.scss';
import Link from 'next/link';

export default function PlaceReviews({ placeId }: { placeId: number }) {
  const { data: reviews, isLoading, isError } = usePlaceReviewsQuery(placeId);

  // TODO: 무한스크롤, 이미지 슬라이드
  return (
    <div className={s.root}>
      {reviews?.length ? (
        <ul className={s.review__list}>
          {reviews.map((review) => (
            <li key={review.id} className={s.review__item}>
              <div className={s.header}>
                <div className={s.header__profile}>
                  <img src={review.member.prof_img} alt={review.member.name} />
                </div>
                <Link href={`/member/${review.member.id}`}>
                  <p className={s.review__title}>{review.member.name}</p>
                </Link>
              </div>
              <div className={s.review__image}>
                <img
                  src={review.images[0]}
                  alt={`${review.place.name}-${review.id}`}
                />
              </div>
              <div className={s.tags}>
                {review.tags.map((tag) => (
                  <div key={tag.id} className={s.tag}>
                    <span>
                      {tag.emoji} {tag.message}
                    </span>
                  </div>
                ))}
              </div>
            </li>
          ))}
        </ul>
      ) : (
        <p className={s.hasNoReview}>아직 리뷰가 없습니다</p>
      )}
    </div>
  );
}
