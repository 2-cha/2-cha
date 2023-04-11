import { useEffect, useRef, useState } from 'react';

export function useIntersect(
  onIntersect: () => void,
  options?: IntersectionObserverInit
) {
  const [ref, setRef] = useState<Element | null>(null);
  const callback = useRef(onIntersect);

  useEffect(() => {
    if (!ref) return;

    const observer = new IntersectionObserver(
      (entries: IntersectionObserverEntry[]) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            callback.current();
          }
        });
      },
      options
    );
    observer.observe(ref);
    return () => observer.unobserve(ref);
  }, [ref, options]);

  return { ref: setRef };
}
