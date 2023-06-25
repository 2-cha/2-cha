import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';

export function useQueryParamState(key: string) {
  const router = useRouter();
  const queryParam = router.query[key];
  const initialState = Array.isArray(queryParam) ? queryParam[0] : queryParam;

  const [state, setState] = useState<string>(initialState || '');
  useEffect(() => {
    if (initialState) {
      setState(initialState);
    }
  }, [initialState]);

  return [state, setState] as const;
}
