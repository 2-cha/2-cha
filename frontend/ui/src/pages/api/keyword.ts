import { fetchKakao } from '@/lib/kakao';
import { NextRequest } from 'next/server';

export const config = {
  runtime: 'edge',
};

export interface Place {
  id: string;
  place_name: string;
  phone: string;
  address_name: string;
  road_address_name: string;
  x: string;
  y: string;
  category_group_code: string;
  category_group_name: string;
  category_name: string;
}

export default async function handler(req: NextRequest) {
  try {
    const { searchParams } = new URL(req.url);
    const data = await fetchKakao<Place>(
      `/local/search/keyword.json?${searchParams.toString()}`
    );

    return new Response(JSON.stringify(data), {
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
