import { GetServerSidePropsContext } from 'next';
import { Place, QueryResponse } from '@/types';
import { fetchServer } from '@/lib/fetchServer';

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

interface PlaceInfoProps {
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

export default function PlaceInfo({ placeInfo }: PlaceInfoProps) {
  return (
    <div>
      <h1>{placeInfo.name}</h1>
      <p>{placeInfo.address}</p>
      {placeInfo.tags.map((tag) => (
        <span key={tag.id}>{tag.message}</span>
      ))}
    </div>
  );
}
