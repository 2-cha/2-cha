import { Tag } from './tag';

export interface Member {
  id: number;
  name: string;
  prof_img: string;
  prof_msg: string;
}

export interface Review {
  id: number;
  member: Member;
  images: string[];
  tags: Tag[];
}
