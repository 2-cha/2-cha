export interface Tag {
  id: number;
  emoji: string;
  message: string;
  count?: number;
  category?: string;
}

export type CategorizedTag = Record<string, Omit<Tag, 'category'>[]>;

export interface TagWithCount extends Tag {
  count: number;
}
