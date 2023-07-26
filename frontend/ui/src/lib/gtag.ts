export const GA_TRACKING_ID = process.env.NEXT_PUBLIC_GA_ID;

declare global {
  interface Window {
    gtag?: (...args: any[]) => void;
  }
}

function gtag(...args: any[]) {
  if (typeof window === 'undefined') {
    return;
  }

  if (typeof window.gtag === 'function') {
    window.gtag(...args);
  }
}

export function pageview(url: string) {
  gtag('config', GA_TRACKING_ID, {
    page_path: url,
  });
}

export function event({
  name,
  params,
}: {
  name: string;
  params: Record<string, any>;
}) {
  gtag('event', name, params);
}
