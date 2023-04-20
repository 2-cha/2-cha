import { rest } from 'msw';

export const kakaoHandlers = [
  rest.get('/api/region', (req, res, ctx) => {
    const { lon, lat } = req.params;

    return res(
      ctx.status(200),
      ctx.json({
        region_type: 'B',
        code: '1168010300',
        address_name: '서울특별시 강남구 개포동',
        region_1depth_name: '서울특별시',
        region_2depth_name: '강남구',
        region_3depth_name: '개포동',
        region_4depth_name: '',
        x: lon,
        y: lat,
      })
    );
  }),
];
