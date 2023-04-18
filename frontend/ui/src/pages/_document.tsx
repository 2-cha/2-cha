import { Html, Head, Main, NextScript } from 'next/document';
import Script from 'next/script';

export default function Document() {
  return (
    <Html lang="ko">
      <Head />
      <body>
        <script
          dangerouslySetInnerHTML={{
            __html: `
              (function() {
                function setTheme(newTheme) {
                  if (newTheme === 'dark') {
                    document.documentElement.setAttribute('data-theme', 'dark');
                  } else {
                    document.documentElement.setAttribute('data-theme', 'light');
                  }
                  localStorage.setItem('theme', newTheme);
                }
                window.__setTheme = setTheme;

                let preferredTheme;
                try {
                  preferredTheme = localStorage.getItem('theme');
                } catch (e) {}

                let initialTheme = preferredTheme;
                let darkQuery = window.matchMedia('(prefers-color-scheme: dark)');

                if (!initialTheme) {
                  initialTheme = darkQuery.matches ? 'dark' : 'light';
                }

                setTheme(initialTheme);

                darkQuery.addListener(function(e) {
                  if (!preferredTheme) {
                    setTheme(e.matches ? 'dark' : 'light');
                  }
                });
              })();
            `,
          }}
        />
        <Script
          src={
            '//dapi.kakao.com/v2/maps/sdk.js' +
            `?appkey=${process.env.NEXT_PUBLIC_KAKAO_MAP_API_KEY}&libraries=services,clusterer&autoload=false`
          }
          strategy="beforeInteractive"
        />
        <Main />
        <NextScript />
      </body>
    </Html>
  );
}
