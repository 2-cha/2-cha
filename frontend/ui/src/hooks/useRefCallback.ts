import { useRef } from 'react';

export function useRefCallback<T extends (...args: any[]) => any>(
  callback: T | undefined
) {
  const ref = useRef(callback);
  ref.current = callback;

  return ref.current;
}
