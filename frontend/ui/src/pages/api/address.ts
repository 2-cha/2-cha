import { fetchKakao } from '@/lib/kakao';
import { NextRequest } from 'next/server';

export const config = {
  runtime: 'edge',
};

interface AddressInfo {
  address_name: string;
  region_1depth_name: string;
  region_2depth_name: string;
  region_3depth_name: string;
  region_3depth_h_name: string;
  h_code: string;
  b_code: string;
  mountain_yn: string;
  main_address_no: string;
  sub_address_no: string;
  x: string;
  y: string;
}

interface RoadAddressInfo {
  address_name: string;
  region_1depth_name: string;
  region_2depth_name: string;
  region_3depth_name: string;
  road_name: string;
  underground_yn: string;
  main_building_no: string;
  sub_building_no: string;
  building_name: string;
  zone_no: string;
  x: string;
  y: string;
}

export interface Address {
  address_name: string;
  address_type: 'REGION' | 'ROAD' | 'REGION_ADDR' | 'ROAD_ADDR';
  x: string;
  y: string;
  address: AddressInfo;
  road_address: RoadAddressInfo;
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
