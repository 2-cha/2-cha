import { NextRequest } from 'next/server';

export const config = {
  runtime: 'experimental-edge',
  matcher: ['/proxy/api/:path*'],
};

const API_PROXY_PATH = '/proxy/api';

export default function middleware(req: NextRequest) {
  /*
   * development, preview 환경 모두에서 CORS 문제를 피하기 위해 설정
   * cf에서 아직 rewrite를 지원하지 않는다
   */
  if (req.nextUrl.pathname.startsWith(API_PROXY_PATH)) {
    if (process.env.NEXT_PUBLIC_BASE_API_URL === undefined) {
      return new Response(
        JSON.stringify({ error: 'NEXT_PUBLIC_BASE_API_URL is not defined' }),
        {
          status: 500,
          headers: { 'Content-Type': 'application/json' },
        }
      );
    }

    const serverUrl =
      req.nextUrl.pathname.replace(
        API_PROXY_PATH,
        process.env.NEXT_PUBLIC_BASE_API_URL
      ) + req.nextUrl.search;
    return fetch(serverUrl, {
      method: req.method,
      headers: req.headers,
      body: req.body,
    });
  }
}
