import Head from 'next/head';
import { useEffect, useState } from 'react';

export interface MetaDataProps {
  title?: string;
  description?: string;
}

export default function MetaData({
  title,
  description = '2차 어디가지',
}: MetaDataProps) {
  const [backgroundColor, setBackgroundColor] = useState('black');

  useEffect(() => {
    const backgroundColorValue = getComputedStyle(
      document.documentElement
    ).getPropertyValue('--background-color');
    setBackgroundColor(backgroundColorValue);
  }, []);
  const pageTitle = title ? `${title} - 2cha` : '2cha';
  return (
    <Head>
      <title>{pageTitle}</title>
      <meta name="description" content={description} />

      <meta name="viewport" content="width=device-width, initial-scale=1" />
      <link rel="manifest" href="/manifest.json" />
      <link rel="shortcut icon" href="/favicon.ico" />
      <link rel="apple-touch-icon" sizes="72x72" href="/images/icons-72.png" />
      <link
        rel="apple-touch-icon"
        sizes="120x120"
        href="/images/icons-120.png"
      />
      <link
        rel="apple-touch-icon"
        sizes="144x144"
        href="/images/icons-144.png"
      />
      <link
        rel="apple-touch-icon"
        sizes="180x180"
        href="/images/icons-180.png"
      />
      <link
        rel="icon"
        type="image/png"
        sizes="192x192"
        href="/images/icons-192.png"
      />
      <link
        rel="icon"
        type="image/png"
        sizes="96x96"
        href="/images/icons-96.png"
      />
      <link
        rel="icon"
        type="image/png"
        sizes="16x16"
        href="/images/icons-16.png"
      />

      <meta name="theme-color" content={backgroundColor} />
      <meta name="apple-touch-fullscreen" content="yes" />
      <meta name="apple-mobile-web-app-capable" content="yes" />
      <meta name="apple-mobile-web-app-title" content="2cha" />

      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 430px) and (device-height: 932px) and (-webkit-device-pixel-ratio: 3) and (orientation: landscape)"
        href="/images/splash/iPhone_14_Pro_Max_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 393px) and (device-height: 852px) and (-webkit-device-pixel-ratio: 3) and (orientation: landscape)"
        href="/images/splash/iPhone_14_Pro_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 428px) and (device-height: 926px) and (-webkit-device-pixel-ratio: 3) and (orientation: landscape)"
        href="/images/splash/iPhone_14_Plus__iPhone_13_Pro_Max__iPhone_12_Pro_Max_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 390px) and (device-height: 844px) and (-webkit-device-pixel-ratio: 3) and (orientation: landscape)"
        href="/images/splash/iPhone_14__iPhone_13_Pro__iPhone_13__iPhone_12_Pro__iPhone_12_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 375px) and (device-height: 812px) and (-webkit-device-pixel-ratio: 3) and (orientation: landscape)"
        href="/images/splash/iPhone_13_mini__iPhone_12_mini__iPhone_11_Pro__iPhone_XS__iPhone_X_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 3) and (orientation: landscape)"
        href="/images/splash/iPhone_11_Pro_Max__iPhone_XS_Max_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)"
        href="/images/splash/iPhone_11__iPhone_XR_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 414px) and (device-height: 736px) and (-webkit-device-pixel-ratio: 3) and (orientation: landscape)"
        href="/images/splash/iPhone_8_Plus__iPhone_7_Plus__iPhone_6s_Plus__iPhone_6_Plus_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 375px) and (device-height: 667px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)"
        href="/images/splash/iPhone_8__iPhone_7__iPhone_6s__iPhone_6__4.7__iPhone_SE_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 320px) and (device-height: 568px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)"
        href="/images/splash/4__iPhone_SE__iPod_touch_5th_generation_and_later_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 1024px) and (device-height: 1366px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)"
        href="/images/splash/12.9__iPad_Pro_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 834px) and (device-height: 1194px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)"
        href="/images/splash/11__iPad_Pro__10.5__iPad_Pro_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 820px) and (device-height: 1180px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)"
        href="/images/splash/10.9__iPad_Air_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 834px) and (device-height: 1112px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)"
        href="/images/splash/10.5__iPad_Air_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 810px) and (device-height: 1080px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)"
        href="/images/splash/10.2__iPad_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 768px) and (device-height: 1024px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)"
        href="/images/splash/9.7__iPad_Pro__7.9__iPad_mini__9.7__iPad_Air__9.7__iPad_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 744px) and (device-height: 1133px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)"
        href="/images/splash/8.3__iPad_Mini_landscape.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 430px) and (device-height: 932px) and (-webkit-device-pixel-ratio: 3) and (orientation: portrait)"
        href="/images/splash/iPhone_14_Pro_Max_portrait.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 393px) and (device-height: 852px) and (-webkit-device-pixel-ratio: 3) and (orientation: portrait)"
        href="/images/splash/iPhone_14_Pro_portrait.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 428px) and (device-height: 926px) and (-webkit-device-pixel-ratio: 3) and (orientation: portrait)"
        href="/images/splash/iPhone_14_Plus__iPhone_13_Pro_Max__iPhone_12_Pro_Max_portrait.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 390px) and (device-height: 844px) and (-webkit-device-pixel-ratio: 3) and (orientation: portrait)"
        href="/images/splash/iPhone_14__iPhone_13_Pro__iPhone_13__iPhone_12_Pro__iPhone_12_portrait.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 375px) and (device-height: 812px) and (-webkit-device-pixel-ratio: 3) and (orientation: portrait)"
        href="/images/splash/iPhone_13_mini__iPhone_12_mini__iPhone_11_Pro__iPhone_XS__iPhone_X_portrait.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 3) and (orientation: portrait)"
        href="/images/splash/iPhone_11_Pro_Max__iPhone_XS_Max_portrait.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)"
        href="/images/splash/iPhone_11__iPhone_XR_portrait.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 414px) and (device-height: 736px) and (-webkit-device-pixel-ratio: 3) and (orientation: portrait)"
        href="/images/splash/iPhone_8_Plus__iPhone_7_Plus__iPhone_6s_Plus__iPhone_6_Plus_portrait.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 375px) and (device-height: 667px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)"
        href="/images/splash/iPhone_8__iPhone_7__iPhone_6s__iPhone_6__4.7__iPhone_SE_portrait.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 320px) and (device-height: 568px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)"
        href="/images/splash/4__iPhone_SE__iPod_touch_5th_generation_and_later_portrait.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 1024px) and (device-height: 1366px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)"
        href="/images/splash/12.9__iPad_Pro_portrait.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 834px) and (device-height: 1194px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)"
        href="/images/splash/11__iPad_Pro__10.5__iPad_Pro_portrait.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 820px) and (device-height: 1180px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)"
        href="/images/splash/10.9__iPad_Air_portrait.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 834px) and (device-height: 1112px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)"
        href="/images/splash/10.5__iPad_Air_portrait.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 810px) and (device-height: 1080px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)"
        href="/images/splash/10.2__iPad_portrait.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 768px) and (device-height: 1024px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)"
        href="/images/splash/9.7__iPad_Pro__7.9__iPad_mini__9.7__iPad_Air__9.7__iPad_portrait.png"
      />
      <link
        rel="apple-touch-startup-image"
        media="screen and (device-width: 744px) and (device-height: 1133px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)"
        href="/images/splash/8.3__iPad_Mini_portrait.png"
      />

      <meta property="og:type" content="website" />
      <meta property="og:url" content="https://2cha.place" />
      <meta property="og:title" content={pageTitle} />
      <meta property="og:description" content={description} />
    </Head>
  );
}
