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
  rest.get('api/address', (req, res, ctx) => {
    const { query } = req.params;

    if (!query) {
      return res(
        ctx.status(400),
        ctx.json({
          error: 'query is required',
        })
      );
    }

    return res(
      ctx.status(200),
      ctx.json([
        {
          address: {
            address_name: '충남 천안시 동남구 동면',
            b_code: '4413137000',
            h_code: '4413137000',
            main_address_no: '',
            mountain_yn: 'N',
            region_1depth_name: '충남',
            region_2depth_name: '천안시 동남구',
            region_3depth_h_name: '동면',
            region_3depth_name: '동면',
            sub_address_no: '',
            x: '127.344304936236',
            y: '36.7761935172924',
          },
          address_name: '충남 천안시 동남구 동면',
          address_type: 'REGION',
          road_address: null,
          x: '127.344304936236',
          y: '36.7761935172924',
        },
        {
          address: {
            address_name: '세종특별자치시 연동면',
            b_code: '3611032000',
            h_code: '3611032000',
            main_address_no: '',
            mountain_yn: 'N',
            region_1depth_name: '세종특별자치시',
            region_2depth_name: '',
            region_3depth_h_name: '연동면',
            region_3depth_name: '연동면',
            sub_address_no: '',
            x: '127.327345245798',
            y: '36.5552990017928',
          },
          address_name: '세종특별자치시 연동면',
          address_type: 'REGION',
          road_address: null,
          x: '127.327345245798',
          y: '36.5552990017928',
        },
        {
          address: {
            address_name: '강원 양구군 동면',
            b_code: '4280032000',
            h_code: '4280032000',
            main_address_no: '',
            mountain_yn: 'N',
            region_1depth_name: '강원',
            region_2depth_name: '양구군',
            region_3depth_h_name: '동면',
            region_3depth_name: '동면',
            sub_address_no: '',
            x: '128.04349997028',
            y: '38.2023166396045',
          },
          address_name: '강원 양구군 동면',
          address_type: 'REGION',
          road_address: null,
          x: '128.04349997028',
          y: '38.2023166396045',
        },
        {
          address: {
            address_name: '강원 춘천시 동면',
            b_code: '4211031000',
            h_code: '4211031000',
            main_address_no: '',
            mountain_yn: 'N',
            region_1depth_name: '강원',
            region_2depth_name: '춘천시',
            region_3depth_h_name: '동면',
            region_3depth_name: '동면',
            sub_address_no: '',
            x: '127.781091988381',
            y: '37.910341608957',
          },
          address_name: '강원 춘천시 동면',
          address_type: 'REGION',
          road_address: null,
          x: '127.781091988381',
          y: '37.910341608957',
        },
        {
          address: {
            address_name: '강원 정선군 화암면',
            b_code: '4277036000',
            h_code: '4277036000',
            main_address_no: '',
            mountain_yn: 'N',
            region_1depth_name: '강원',
            region_2depth_name: '정선군',
            region_3depth_h_name: '화암면',
            region_3depth_name: '화암면',
            sub_address_no: '',
            x: '128.787787615915',
            y: '37.3401268285408',
          },
          address_name: '강원 정선군 화암면',
          address_type: 'REGION',
          road_address: null,
          x: '128.787787615915',
          y: '37.3401268285408',
        },
        {
          address: {
            address_name: '강원 홍천군 영귀미면',
            b_code: '4272035200',
            h_code: '4272035200',
            main_address_no: '',
            mountain_yn: 'N',
            region_1depth_name: '강원',
            region_2depth_name: '홍천군',
            region_3depth_h_name: '영귀미면',
            region_3depth_name: '영귀미면',
            sub_address_no: '',
            x: '127.935063630646',
            y: '37.6780725461922',
          },
          address_name: '강원 홍천군 영귀미면',
          address_type: 'REGION',
          road_address: null,
          x: '127.935063630646',
          y: '37.6780725461922',
        },
        {
          address: {
            address_name: '전남 화순군 동면',
            b_code: '4679042000',
            h_code: '4679042000',
            main_address_no: '',
            mountain_yn: 'N',
            region_1depth_name: '전남',
            region_2depth_name: '화순군',
            region_3depth_h_name: '동면',
            region_3depth_name: '동면',
            sub_address_no: '',
            x: '127.038516272952',
            y: '35.0299153952786',
          },
          address_name: '전남 화순군 동면',
          address_type: 'REGION',
          road_address: null,
          x: '127.038516272952',
          y: '35.0299153952786',
        },
        {
          address: {
            address_name: '경남 양산시 동면',
            b_code: '4833031000',
            h_code: '4833031000',
            main_address_no: '',
            mountain_yn: 'N',
            region_1depth_name: '경남',
            region_2depth_name: '양산시',
            region_3depth_h_name: '동면',
            region_3depth_name: '동면',
            sub_address_no: '',
            x: '129.061860910264',
            y: '35.3235198494618',
          },
          address_name: '경남 양산시 동면',
          address_type: 'REGION',
          road_address: null,
          x: '129.061860910264',
          y: '35.3235198494618',
        },
      ])
    );
  }),
];
