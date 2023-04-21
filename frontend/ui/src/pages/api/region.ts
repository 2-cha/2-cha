import { NextRequest } from 'next/server';

const KAKAO_API_URL = 'https://dapi.kakao.com';

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

interface RegionResponse {
  meta: {
    total_count: number;
  };
  documents: Region[];
}

export const config = {
  runtime: 'edge',
};

export default async function handler(req: NextRequest) {
  try {
    const { searchParams } = new URL(req.url);
    const params = new URLSearchParams({
      x: searchParams.get('lon') as string,
      y: searchParams.get('lat') as string,
    });
    const response = await fetch(
      `${KAKAO_API_URL}/v2/local/geo/coord2regioncode.json?${params.toString()}`,
      {
        headers: {
          Authorization: `KakaoAK ${process.env.KAKAO_REST_API_KEY}`,
        },
      }
    );

    if (!response.ok) {
      return response;
    }

    const data: RegionResponse = await response.json();
    if (data.meta.total_count === 0) {
      return new Response('No region found', { status: 404 });
    }

    return new Response(JSON.stringify(data.documents[0]), {
      status: 200,
      headers: { 'Content-Type': 'application/json' },
    });
  } catch (e) {
    return new Response('Internal Server Error', { status: 500 });
  }
}
