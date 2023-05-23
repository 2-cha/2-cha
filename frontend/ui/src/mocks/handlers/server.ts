import { rest } from 'msw';
import {
  placesMockData,
  type PlaceSearchResult,
  type Place,
  type QueryResponse,
  type Review,
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
    const placeId = req.params.placeId;

    if (placeId == null) {
      return res(
        ctx.status(400),
        ctx.json<QueryResponse<Place>>({
          success: false,
          code: '400',
          status: 'Bad Request',
          message: 'placeId is required',
        })
      );
    }

    return res(
      ctx.status(200),
      ctx.json<QueryResponse<Place>>({
        success: true,
        status: 'OK',
        data: { ...place, id: Number(placeId) },
      })
    );
  }),

  rest.get(`${BASE_URL}/places/:placeId/reviews`, (_req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json<QueryResponse<Review[]>>({
        success: true,
        status: 'OK',
        data: reviews,
      })
    );
  }),
];

const place = {
  id: 1,
  name: '이노베이션 아카데미',
  address: '서울 강남구 개포로 416',
  lot_address: '서울 강남구 개포동 14-1',
  site: 'https://42seoul.kr/',
  lat: 37.4879759679358,
  lon: 127.065527640082,
  category: '클러스터',
  thumbnail: 'https://picsum.photos/420/420',
  tags: [],
};

const member = {
  id: 1,
  name: 'seushin',
  prof_img: 'https://picsum.photos/420/420',
  prof_msg: 'hello world',
};

const review: Review = {
  id: 1,
  member: member,
  place: place,
  tags: [
    {
      id: 1,
      emoji: '🍺',
      message: '맥주',
    },
    {
      id: 2,
      emoji: '👍',
      message: '좋아요',
    },
  ],
  images: [
    'https://picsum.photos/320/480',
    'https://picsum.photos/320/480',
    'https://picsum.photos/320/480',
  ],
};

const reviews: Review[] = [
  ...Array.from({ length: 12 }).map((_, i) => ({
    ...review,
    id: i,
  })),
];
