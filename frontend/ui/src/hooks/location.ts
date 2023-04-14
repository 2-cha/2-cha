import { useRecoilState } from 'recoil';
import { locationState } from '@/atoms/location';
import { useCallback, useEffect, useState } from 'react';

const LOCATION_KEY = 'location';

export function useCurrentLocation() {
  const [location, setLocation] = useRecoilState(locationState);
  const [isLoading, setIsLoading] = useState(false);
  const [isError, setIsError] = useState(false);

  const requestLocation = useCallback(
    function requestLocation() {
      setIsLoading(true);

      navigator.geolocation.getCurrentPosition(
        (position) => {
          const loc = {
            lat: position.coords.latitude,
            lon: position.coords.longitude,
          };
          setLocation(loc);
          try {
            localStorage.setItem(LOCATION_KEY, JSON.stringify(loc));
          } catch {}
          setIsLoading(false);
        },
        () => {
          setIsLoading(false);
          setIsError(true);
        }
      );
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
