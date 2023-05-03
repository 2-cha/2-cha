import type { Tag, Member } from '.';

export interface Review {
  id: number;
  member: Member;
  images: string[];
  tags: Tag[];
}
