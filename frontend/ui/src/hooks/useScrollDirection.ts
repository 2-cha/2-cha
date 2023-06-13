import { useEffect, useState } from 'react';

interface ScrollDirectionParams {
  offset?: number;
  initialDirection?: 'up' | 'down';
}

export function useScrollDirection({
  offset,
  initialDirection = 'up',
}: ScrollDirectionParams = {}) {
  const [scrollDirection, setScrollDirection] = useState<'up' | 'down'>(
    initialDirection
  );

  useEffect(() => {
    if (typeof window === 'undefined') {
      return;
    }

    let lastScrollTop = 0;
    let ticking = false;

    const handleScroll = () => {
      const scrollTop = window.pageYOffset;
      const scrollDirection = scrollTop > lastScrollTop ? 'down' : 'up';

      if (!offset || offset < Math.abs(scrollTop - lastScrollTop)) {
        if (!ticking) {
          window.requestAnimationFrame(() => {
            setScrollDirection(scrollDirection);
            ticking = false;
          });

          ticking = true;
        }
      }

      lastScrollTop = scrollTop;
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, [offset]);

  return scrollDirection;
}
