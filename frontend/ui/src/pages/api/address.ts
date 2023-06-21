import { fetchKakao } from '@/lib/kakao';
import { NextRequest } from 'next/server';

export const config = {
  runtime: 'edge',
};

export interface Address {
  address_name: string;
  address_type: string; // 'REGION' | 'ROAD' | 'REGION_ADDR' | 'ROAD_ADDR'
  x: string;
  y: string;
  address: unknown;
  road_address: unknown;
}

export default async function handler(req: NextRequest) {
  try {
    const { searchParams } = new URL(req.url);
    const data = await fetchKakao<Address>(
      `/local/search/address.json?${searchParams.toString()}`
    );

    return new Response(JSON.stringify(data.documents), {
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
