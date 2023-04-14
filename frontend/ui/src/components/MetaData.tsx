import Head from 'next/head';

export interface MetaDataProps {
  title?: string;
  description?: string;
}

export default function MetaData({
  title,
  description = '2차 어디가지',
}: MetaDataProps) {
  const pageTitle = title ? `${title} - 2cha` : '2cha';
  return (
    <Head>
      <title>{pageTitle}</title>
      <meta name="description" content={description} />

      <meta name="viewport" content="width=device-width, initial-scale=1" />

      <meta property="og:type" content="website" />
      <meta property="og:url" content="https://2cha.place" />
      <meta property="og:title" content={pageTitle} />
      <meta property="og:description" content={description} />
    </Head>
  );
}
