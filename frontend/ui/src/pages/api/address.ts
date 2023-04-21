import { NextRequest } from 'next/server';

const KAKAO_API_URL = 'https://dapi.kakao.com';

export interface Address {
  address_name: string;
  address_type: string; // 'REGION' | 'ROAD' | 'REGION_ADDR' | 'ROAD_ADDR'
  x: string;
  y: string;
  address: unknown;
  road_address: unknown;
}

interface AddressResponse {
  documents: Address[];
  meta: {
    is_end: boolean;
    pageable_count: number;
    total_count: number;
  };
}

export const config = {
  runtime: 'edge',
};

export default async function handler(req: NextRequest) {
  try {
    const { searchParams } = new URL(req.url);
    const params = new URLSearchParams({
      query: searchParams.get('query') || '',
    });
    const response = await fetch(
      `${KAKAO_API_URL}/v2/local/search/address.json?${params.toString()}`,
      {
        headers: {
          Authorization: `KakaoAK ${process.env.KAKAO_REST_API_KEY}`,
        },
      }
    );

    if (!response.ok) {
      return response;
    }

    const data: AddressResponse = await response.json();
    return new Response(JSON.stringify(data.documents), {
      status: 200,
      headers: { 'Content-Type': 'application/json' },
    });
  } catch (e) {
    return new Response('Internal Server Error', { status: 500 });
  }
}
