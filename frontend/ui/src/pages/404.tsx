import MetaData from '@/components/MetaData';
import Error from '@/components/Error';

export default function NotFoundPage() {
  return (
    <>
      <MetaData title="404" />
      <Error title="404" description="Not Found" />
    </>
  );
}
