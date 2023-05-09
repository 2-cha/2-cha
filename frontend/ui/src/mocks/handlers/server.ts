import { rest } from 'msw';
import {
  placesMockData,
  type PlaceSearchResult,
  type Place,
  type QueryResponse,
  Review,
} from '@/types';

const BASE_URL = process.env.NEXT_PUBLIC_BASE_API_URL;
let id = 0;

export const serverHandlers = [
  rest.get(`${BASE_URL}/places/nearby`, (req, res, ctx) => {
    const min_dist = req.url.searchParams.get('min_dist');

    return res(
      ctx.status(200),
      ctx.json<QueryResponse<PlaceSearchResult[]>>({
        success: true,
        status: 'OK',
        data: placesMockData.map<PlaceSearchResult>((place) => ({
          ...place,
          id: id++,
          distance: Number(min_dist),
          lat: 37.4879759679358,
          lon: 127.065527640082,
        })),
      })
    );
  }),
  rest.get(`${BASE_URL}/places/:placeId`, (req, res, ctx) => {
    const placeId = req.url.searchParams.get('placeId');

    return res(
      ctx.status(200),
      ctx.json<QueryResponse<Place>>({
        success: true,
        status: 'OK',
        data: {
          id: Number(placeId),
          name: '이노베이션 아카데미',
          address: '서울 강남구 개포로 416',
          lot_address: '서울 강남구 개포동 14-1',
          site: 'https://42seoul.kr/',
          lat: 37.4879759679358,
          lon: 127.065527640082,
          category: '클러스터',
          thumbnail: 'https://picsum.photos/420/420',
          tags: [],
        },
      })
    );
  }),
  rest.get(`${BASE_URL}/places/:placeId/reviews`, (_req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json<QueryResponse<Review[]>>({
        success: true,
        status: 'OK',
        data: [],
      })
    );
  }),
];
