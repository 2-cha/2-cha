import { useEffect, useRef, useState } from 'react';

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
  const [isIntersecting, setIntersecting] = useState(!!initialState);

  const callback = useRef<IntersectionObserverOptions['onChange']>();
  callback.current = onChange;

  useEffect(() => {
    if (!ref) return;

    const observer = new IntersectionObserver(
      (entries: IntersectionObserverEntry[]) => {
        entries.forEach((entry) => {
          if (entry.target === ref) {
            setIntersecting(entry.isIntersecting);
            if (callback.current) {
              callback.current(entry.isIntersecting);
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
