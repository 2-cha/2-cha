import { useRouter } from 'next/router';
import { useCallback } from 'react';

function queryString(obj: Record<string, any>) {
  return new URLSearchParams(
    Object.entries(obj)
      .filter(([_key, value]) => value != null)
      .map(([key, value]) => {
        if (Array.isArray(value)) {
          return value.map((item) => [key, item]);
        }

        return [[key, value]];
      })
      .flat()
  ).toString();
}

export function useQueryParam({
  key,
  defaultValue,
}: {
  key: string;
  defaultValue: string;
}) {
  const router = useRouter();
  let queryParam = router.query[key];
  queryParam = Array.isArray(queryParam) ? queryParam[0] : queryParam;

  const value = queryParam ?? defaultValue;
  const setValue = useCallback(
    (newValue: string) => {
      const { query: currentQuery } = router;
      const query = { ...currentQuery, [key]: newValue };

      router.replace(`?${queryString(query)}`, undefined, { shallow: true });
    },
    [router, key]
  );

  return [value, setValue] as const;
}
