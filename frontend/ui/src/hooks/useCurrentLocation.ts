import { useRecoilState } from 'recoil';
import { locationState } from '@/atoms/location';
import { useCallback, useEffect, useState } from 'react';

const LOCATION_KEY = 'location';

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
        try {
          localStorage.setItem(LOCATION_KEY, JSON.stringify(loc));
        } catch {}
      } catch (e) {
        setIsError(true);
        try {
          localStorage.removeItem(LOCATION_KEY);
        } catch {}
      }

      setIsLoading(false);
    },
    [setLocation]
  );

  useEffect(() => {
    if (!location && !isLoading) {
      try {
        // geolocation을 가져오는 동안에는 이전 위치를 보여준다.
        const stored = localStorage.getItem(LOCATION_KEY);
        if (stored) {
          setLocation(JSON.parse(stored));
        }
      } catch {}
      requestLocation();
    }
  }, [location, isLoading, setLocation, requestLocation]);

  return { location, isLoading, isError, refresh: requestLocation };
}
