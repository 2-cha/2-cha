import { BookmarkStatus } from './bookmark';
import { TagWithCount } from './tag';

export interface Place {
  id: number;
  name: string;
  category: string;
  address: string;
  image?: string;
  tags?: TagWithCount[];
  tag_summary?: TagWithCount[];
  lat: number;
  lon: number;
  site?: string;
  lot_address?: string;
  bookmark_status: BookmarkStatus;
}

export interface PlaceSearchResult extends Place {
  distance: number;
}
export type SuggestionPlace = Pick<
  PlaceSearchResult,
  'id' | 'name' | 'address' | 'category' | 'distance'
>;
