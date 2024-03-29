import { rest } from 'msw';
import {
  type PlaceSearchResult,
  type Place,
  type Review,
  type Member,
} from '@/types';

const BASE_URL = process.env.NEXT_PUBLIC_BASE_API_URL;
let id = 0;

function success<T>(data: T) {
  return {
    success: true,
    status: 'OK',
    data: data,
  };
}

function fail(message: string) {
  return {
    success: false,
    code: 400,
    status: 'Bad Request',
    message: message,
  };
}

export const serverHandlers = [
  rest.get(`${BASE_URL}/places/nearby`, (req, res, ctx) => {
    const min_dist = req.url.searchParams.get('min_dist');

    return res(
      ctx.status(200),
      ctx.json(
        success(
          Array.from({ length: 12 })
            .map(() => place)
            .map<PlaceSearchResult>((place) => ({
              ...place,
              id: id++,
              distance: Number(min_dist),
            }))
        )
      )
    );
  }),

  rest.get(`${BASE_URL}/places`, (_req, res, ctx) => {
    return res(ctx.status(200), ctx.json(success([place])));
  }),

  rest.get(`${BASE_URL}/places/:placeId`, (req, res, ctx) => {
    const placeId = req.params.placeId;

    if (placeId == null) {
      return res(ctx.status(400), ctx.json(fail('placeId is required')));
    }

    return res(
      ctx.status(200),
      ctx.json(success({ ...place, id: Number(placeId) }))
    );
  }),

  rest.get(`${BASE_URL}/places/:placeId/reviews`, (_req, res, ctx) => {
    return res(ctx.status(200), ctx.json(success(reviews)));
  }),

  rest.post(BASE_URL + '/places/:placeId/reviews', async (req, res, ctx) => {
    const body = await req.json();
    reviews.push({
      id: reviews.length,
      member: member,
      place: place,
      tags: body.tag_ids.map((id: number) => ({
        id: id,
        emoji: '🍺',
        message: '맥주',
      })),
      images: body.img_urls,
    });

    return res(ctx.status(200), ctx.json(success(null)));
  }),
  rest.post(BASE_URL + '/reviews/images', (_req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json(
        success({
          url: 'https://picsum.photos/320/480',
          suggestions: [place],
        })
      )
    );
  }),

  rest.post(BASE_URL + '/bookmarks/:itemType/:itemId', (_req, res, ctx) => {
    return res(ctx.status(200), ctx.json(success(null)));
  }),
  rest.delete(BASE_URL + '/bookmarks/:itemType/:itemId', (_req, res, ctx) => {
    return res(ctx.status(200), ctx.json(success(null)));
  }),

  rest.get(BASE_URL + '/tags', (_req, res, ctx) => {
    return res(ctx.status(200), ctx.json(success(tags)));
  }),
  rest.get(BASE_URL + '/tags/categorized', (_req, res, ctx) => {
    return res(ctx.status(200), ctx.json(success(categorizedTags)));
  }),
];

const place: Place = {
  id: 1,
  name: '이노베이션 아카데미',
  category: 'COCKTAIL_BAR',
  address: '서울 강남구 개포로 416',
  lot_address: '서울 강남구 개포동 14-1',
  site: 'https://42seoul.kr/',
  lat: 37.4879759679358,
  lon: 127.065527640082,
  image: 'https://picsum.photos/420/420',
  tags: [],
  bookmark_status: {
    is_bookmarked: false,
    count: 0,
  },
};

const member: Member = {
  id: 1,
  name: 'seushin',
  prof_img: 'https://picsum.photos/420/420',
  prof_msg: 'hello world',
};

const beerTag = {
  emoji: '🍺',
  message: '맥주',
  category: 'DRINK',
  matching_indexes: [],
};

const thumbTag = {
  emoji: '👍',
  message: '좋아요',
  category: 'REACTION',
  matching_indexes: [],
};

const tags = [...Array(15)]
  .map((_, idx) => ({ ...thumbTag, id: idx }))
  .concat([...Array(15)].map((_, idx) => ({ ...beerTag, id: idx + 15 })));

const categorizedTags = {
  DRINK: [...Array(15)].map((_, idx) => ({ ...beerTag, id: idx })),
  REACTION: [...Array(15)].map((_, idx) => ({ ...thumbTag, id: idx })),
};

const review: Review = {
  id: 1,
  member: member,
  place: place,
  tags: tags,
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
