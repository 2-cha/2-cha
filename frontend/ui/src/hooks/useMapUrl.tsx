import type { Place } from '@/types';
import { useMemo } from 'react';

const NAVER_MAP_APP_NAME = '2cha.place';
const roadAddress1Regex =
  /(([가-힣A-Za-z \d~\-\.]{2,}(로|길).[\d]+)|([가-힣A-Za-z \d~\-\.]+(읍|동)\s)[\d]+)/;

export function useNaverMapUrl(place: Place, timeout = 1000) {
  const naverMapUrl = useMemo(() => {
    const roadAddress1 = roadAddress1Regex.exec(place.address);
    if (!roadAddress1) {
      return '#';
    }

    const params = new URLSearchParams({
      query: roadAddress1[0] + ' ' + place.name,
      appname: NAVER_MAP_APP_NAME,
    });

    const naverMapWebUrl =
      'https://map.naver.com/v5/search/' + params.get('query');
    const naverMapIOSUrl = 'nmap://search' + '?' + params.toString();
    const naverMapAndroidUrl =
      'intent://search' +
      '?' +
      params.toString() +
      '#Intent;' +
      Object.entries({
        scheme: 'nmap',
        action: 'android.intent.action.VIEW',
        category: 'android.intent.category.BROWSABLE',
        package: 'com.nhn.android.nmap',
      })
        .map((k, v) => `${k}=${v}`)
        .join(';') +
      ';end';

    switch (getUserPlatform()) {
      case 'ios':
        return naverMapIOSUrl;
      case 'android':
        return naverMapAndroidUrl;
      default:
        return naverMapWebUrl;
    }
  }, [place.address, place.name]);

  return naverMapUrl;
}

function getUserPlatform() {
  if (typeof window === 'undefined') {
    return;
  }

  if (window.navigator.userAgent.match(/(iPhone|iPod|iPad)/i)) {
    return 'ios';
  } else if (window.navigator.userAgent.match(/android/i)) {
    return 'android';
  } else {
    return 'web';
  }
}
