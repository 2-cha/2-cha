import { useRouter } from 'next/router';
import { useEffect } from 'react';

import Layout from '@/components/Layout';

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    router.replace('/places');
  });
  return (
    <Layout>{/* TODO: render skeleton component or do nothing? */}</Layout>
  );
}
