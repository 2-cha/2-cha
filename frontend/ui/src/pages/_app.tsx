import { RecoilRoot } from 'recoil';
import RecoilNexus from 'recoil-nexus';
import type { AppProps } from 'next/app';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import Layout from '@/components/Layout';

import '@/styles/globals.scss';

if (process.env.NEXT_PUBLIC_API_MOCKING === 'enabled') {
  require('@/mocks');
}

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      keepPreviousData: true,
    },
  },
});

export default function App({ Component, pageProps }: AppProps) {
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
