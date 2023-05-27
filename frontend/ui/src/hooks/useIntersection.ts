import { useEffect, useState } from 'react';
import { useRefCallback } from './useRefCallback';

interface IntersectionObserverOptions extends IntersectionObserverInit {
  onChange?: (isIntersecting: boolean) => void;
  initialState?: boolean;
}

export function useIntersection({
  onChange,
  initialState,
  root,
  rootMargin,
  threshold,
}: IntersectionObserverOptions = {}) {
  const [ref, setRef] = useState<Element | null>(null);
  const callback = useRefCallback(onChange);
  const [isIntersecting, setIntersecting] = useState(!!initialState);

  useEffect(() => {
    if (!ref) return;

    const observer = new IntersectionObserver(
      (entries: IntersectionObserverEntry[]) => {
        entries.forEach((entry) => {
          if (entry.target === ref) {
            setIntersecting(entry.isIntersecting);
            if (callback) {
              callback(entry.isIntersecting);
            }
          }
        });
      },
      {
        root,
        rootMargin,
        threshold,
      }
    );

    observer.observe(ref);
    return () => observer.unobserve(ref);
  }, [ref, root, rootMargin, threshold]);

  return { ref: setRef, isIntersecting };
}
