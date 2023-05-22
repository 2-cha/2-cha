import CreateTagButton from './CreateTagButton';
import TagTable from './TagTable';

export interface Tag {
  id: number;
  category: string;
  emoji: string;
  message: string;
}

async function getAllTags(): Promise<Array<Tag>> {
  // TODO: fetch data
  return [
    {
      id: 1,
      category: '분위기',
      emoji: '🌙',
      message: '편안한 분위기',
    },
    {
      id: 2,
      category: '분위기',
      emoji: '🌞',
      message: '활기찬 분위기',
    },
  ];
}

export default async function TagPage() {
  const tags = await getAllTags();

  return (
    <>
      <CreateTagButton />
      <TagTable tags={tags} />
    </>
  );
}
