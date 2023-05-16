export function debounce<T extends any[]>(fn: (...args: T) => void, ms = 300) {
  let timeout: NodeJS.Timeout;
  return function (...args: T) {
    clearTimeout(timeout);
    timeout = setTimeout(() => fn(...args), ms);
  };
}
