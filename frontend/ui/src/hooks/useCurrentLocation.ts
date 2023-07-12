import { useCallback, useEffect, useState } from 'react';
import { useRecoilState } from 'recoil';

import { locationState } from '@/atoms';

async function getCurrentPosition(timeout = 10000) {
  return Promise.race<Promise<GeolocationPosition>[]>([
    new Promise((resolve, reject) => {
      navigator.geolocation.getCurrentPosition(resolve, reject);
    }),
    new Promise((_, reject) => {
      setTimeout(() => {
        reject(new Error('timeout'));
      }, timeout);
    }),
  ]);
}

export function useCurrentLocation() {
  const [location, setLocation] = useRecoilState(locationState);
  const [isLoading, setIsLoading] = useState(false);
  const [isError, setIsError] = useState(false);

  const requestLocation = useCallback(
    async function requestLocation() {
      setIsLoading(true);

      try {
        const position = await getCurrentPosition();
        const loc = {
          lat: position.coords.latitude,
          lon: position.coords.longitude,
        };

        setLocation(loc);
        setIsError(false);
      } catch {
        setIsError(true);
      }

      setIsLoading(false);
    },
    [setLocation]
  );

  useEffect(() => {
    if (!location && !isLoading) {
      requestLocation();
    }
  }, [location, isLoading, requestLocation]);

  return { location, isLoading, isError, refresh: requestLocation };
}
