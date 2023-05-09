import { usePlaceReviewsQuery } from '@/hooks/query/usePlaceReviews';
import s from './PlaceReviewsMenu.module.scss';

export default function PlaceReviews({ placeId }: { placeId: number }) {
  const { data: reviews, isLoading, isError } = usePlaceReviewsQuery(placeId);

  // TODO: 레이아웃 디자인, 무한스크롤
  return (
    <div className={s.root}>
      {reviews?.length ? (
        <ul>
          {reviews.map((review) => (
            <li key={review.id}>{JSON.stringify(review)}</li>
          ))}
        </ul>
      ) : (
        <p className={s.hasNoReview}>아직 리뷰가 없습니다</p>
      )}
    </div>
  );
}
