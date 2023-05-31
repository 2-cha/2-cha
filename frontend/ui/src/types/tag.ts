export interface Tag {
  id: number;
  emoji: string;
  message: string;
  count?: number;
  category?: string;
}

export interface TagWithCount extends Tag {
  count: number;
}
