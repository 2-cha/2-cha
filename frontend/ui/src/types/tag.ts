export interface Tag {
  id: number;
  emoji: string;
  message: string;
}

export interface TagWithCount extends Tag {
  count: number;
}
