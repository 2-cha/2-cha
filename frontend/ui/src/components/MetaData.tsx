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

      <meta property="og:type" content="website" />
      <meta property="og:url" content="https://2cha.place" />
      <meta property="og:title" content={pageTitle} />
      <meta property="og:description" content={description} />
    </Head>
  );
}
