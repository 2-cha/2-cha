import { useRouter } from 'next/router';

export default function CollectionInfoPage() {
  const { query } = useRouter();
  return <div>아아아아 {query.collectionId}</div>;
}
