import { BookmarkStatus } from './bookmark';
import { LikeStatus } from './like';
import { Member } from './member';
import { Review } from './review';

export interface CollectionMetadata {
  id: number;
  title: string;
  thumbnail: string;
  member: Member;
  bookmark_status: BookmarkStatus;
  like_status: LikeStatus;
}

export interface Collection extends CollectionMetadata {
  reviews: Omit<Review, 'member'>[];
}
