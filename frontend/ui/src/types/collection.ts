import { Member } from './member';
import { Review } from './review';

export interface CollectionMetadata {
  id: number;
  title: string;
  thumbnail: string;
  member: Member;
}

export interface Collection extends CollectionMetadata {
  reviews: Omit<Review, 'member'>[];
}
