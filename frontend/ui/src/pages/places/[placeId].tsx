import { GetServerSidePropsContext } from 'next';
import { Place, QueryResponse } from '@/types';
import { fetchServer } from '@/lib/fetchServer';
import NavStackHeader from '@/components/Layout/NavStackHeader';
import MetaData from '@/components/MetaData';
import PlaceInfo from '@/components/PlaceInfo';

// api route를 제외한 runtime은 아직 experimental-edge로 설정해야 함
export const config = {
  runtime: 'experimental-edge',
};

async function fetchPlace(placeId: string) {
  const response = await fetchServer<QueryResponse<Place>>(
    `/places/${placeId}`
  );
  if (!response.success) {
    throw response;
  }
  return response.data;
}

interface PlaceInfoPageProps {
  placeInfo: Place;
}

export async function getServerSideProps(context: GetServerSidePropsContext) {
  try {
    const placeId = context.params!.placeId as string;
    const placeInfo = await fetchPlace(placeId);

    return {
      props: { placeInfo },
    };
  } catch (e) {
    console.error(e);

    // 404 페이지 렌더
    return {
      notFound: true,
    };
  }
}

export default function PlaceInfoPage({ placeInfo }: PlaceInfoPageProps) {
  return (
    <>
      <MetaData title={placeInfo.name} />
      <NavStackHeader />
      <PlaceInfo placeInfo={placeInfo} />
      {/* TODO: 지도, 리뷰 */}
    </>
  );
}
