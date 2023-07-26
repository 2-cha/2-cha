import { RecoilRoot } from 'recoil';
import RecoilNexus from 'recoil-nexus';
import type { AppProps } from 'next/app';
import { useRouter } from 'next/router';
import { useEffect } from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import Layout from '@/components/Layout';
import * as gtag from '@/lib/gtag';

import '@/styles/globals.scss';

if (process.env.NEXT_PUBLIC_API_MOCKING === 'enabled') {
  require('@/mocks');
}

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      keepPreviousData: true,
      retry: 1,
    },
  },
});

export default function App({ Component, pageProps }: AppProps) {
  const router = useRouter();

  useEffect(() => {
    const handleRouteChange = (url: string) => {
      gtag.pageview(url);
    };

    router.events.on('routeChangeComplete', handleRouteChange);
    return () => {
      router.events.off('routeChangeComplete', handleRouteChange);
    };
  }, [router.events]);

  return (
    <QueryClientProvider client={queryClient}>
      <RecoilRoot>
        <RecoilNexus />
        <Layout>
          <Component {...pageProps} />
        </Layout>
      </RecoilRoot>
      <ReactQueryDevtools position="top-right" />
    </QueryClientProvider>
  );
}
