'use server';

export interface Tag {
  id: number;
  category: string;
  emoji: string;
  message: string;
}

export async function getAllTags() {
  // TODO: fetch data
  const tags: Tag[] = [
    {
      id: 1,
      category: "분위기",
      emoji: "🌙",
      message: "편안한 분위기",
    },
    {
      id: 2,
      category: "분위기",
      emoji: "🌞",
      message: "활기찬 분위기",
    },
  ];

  return tags;
}

export async function getTag(id: number) {
  const tags = await getAllTags();
  const tag = tags.find((tag) => tag.id === id);

  return tag;
}

export async function createTag(tag: Tag) { }

export async function editTag(tag: Tag) { }

