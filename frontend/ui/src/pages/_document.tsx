import { Html, Head, Main, NextScript } from 'next/document';

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
        <Main />
        <NextScript />
      </body>
    </Html>
  );
}
