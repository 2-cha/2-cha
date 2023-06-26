export const KAKAO_API_URL = 'https://dapi.kakao.com/v2';

export interface KakaoResponse<T> {
  documents: T[];
  meta: {
    is_end: boolean;
    pageable_count: number;
    total_count: number;
  };
}

export async function fetchKakao<T>(
  url: string,
  options?: RequestInit
): Promise<KakaoResponse<T>> {
  const response = await fetch(KAKAO_API_URL + url, {
    ...options,
    headers: {
      Authorization: `KakaoAK ${process.env.KAKAO_REST_API_KEY}`,
    },
  });

  if (!response.ok) {
    throw response;
  }

  return response.json();
}
