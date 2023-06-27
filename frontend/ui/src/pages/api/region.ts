import { fetchKakao } from '@/lib/kakao';
import { NextRequest } from 'next/server';

export const config = {
  runtime: 'edge',
};

export interface Region {
  region_type: string;
  code: string;
  address_name: string;
  region_1depth_name: string; // 시도 단위
  region_2depth_name: string; // 구 단위
  region_3depth_name: string; // 동 단위
  region_4depth_name: string; // 리 단위
  x: number;
  y: number;
}

export default async function handler(req: NextRequest) {
  try {
    const { searchParams } = new URL(req.url);
    const params = new URLSearchParams({
      x: searchParams.get('lon') as string,
      y: searchParams.get('lat') as string,
    });
    const data = await fetchKakao<Region>(
      `/local/geo/coord2regioncode.json?${params.toString()}`
    );

    if (data.meta.total_count === 0) {
      return new Response('No region found', { status: 404 });
    }

    return new Response(JSON.stringify(data.documents[0]), {
      status: 200,
      headers: { 'Content-Type': 'application/json' },
    });
  } catch (e) {
    if (e instanceof Response) {
      return e;
    } else {
      return new Response('Internal Server Error', { status: 500 });
    }
  }
}
