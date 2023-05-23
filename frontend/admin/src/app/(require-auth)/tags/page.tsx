import { getAllTags } from '@/lib/api';
import CreateTagButton from './CreateTagButton';
import TagTable from './TagTable';

export default async function TagPage() {
  const tags = await getAllTags();

  return (
    <>
      <CreateTagButton />
      <TagTable tags={tags} />
    </>
  );
}
