import type { Tag, Member, Place } from '.';

export interface Review {
  id: number;
  member: Member;
  place: Place;
  images: string[];
  tags: Tag[];
}
