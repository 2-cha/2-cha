import { Review } from '@/types';

import s from './ReviewCard.module.scss';
import Image from 'next/image';

interface Props {
  review: Omit<Review, 'member'>;
}

export default function ReviewCard({ review }: Props) {
  return (
    <div className={s.wrapper}>
      <div>컬렉션 카드</div>
      <Image
        src="https://picsum.photos/536/353"
        alt="collection image"
        width={480}
        height={480}
        className={s.wrapper__image}
      />
      {/* {review.images && review.images[0] ? (
        <Image src={review.images[0]} width={480} height={480} />
      ) : (
        <div>이미지 없음</div>
      )} */}
    </div>
  );
}
