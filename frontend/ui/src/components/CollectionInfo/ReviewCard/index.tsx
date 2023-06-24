import { Review } from '@/types';
import { Collection } from '@/types/collection';

interface Props {
  review: Omit<Review, 'member'>;
}

export default function ReviewCard({ review }: Props) {
  return (
    <div>
      <div>컬렉션 카드</div>
    </div>
  );
}
