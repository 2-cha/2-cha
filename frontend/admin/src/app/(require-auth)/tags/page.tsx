import CreateTagButton from './CreateTagButton';
import TagTable from './TagTable';

async function getTags() {
  return await fetch('/tags/api').then((res) => res.json());
}

export default async function TagPage() {
  const tags = await getTags();

  return (
    <>
      <CreateTagButton />
      <TagTable tags={tags} />
    </>
  );
}
