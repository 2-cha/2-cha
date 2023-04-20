import { memo } from 'react';

export default memo(function ArrowIcon({
  forward,
  width = 24,
  height = 24,
  ...props
}: React.ComponentProps<'svg'> & { forward?: boolean }) {
  let flipx = forward ? 'matrix(-1,0,0,1,0,0)' : undefined;

  return (
    <svg
      width={width}
      height={height}
      viewBox="0 0 153.6 153.6"
      xmlns="http://www.w3.org/2000/svg"
      transform={flipx}
      {...props}
    >
      <path d="M33.6 72h96a4.8 4.8 0 1 1 0 9.6H33.6a4.8 4.8 0 0 1 0 -9.6z" />
      <path d="m35.587 76.8 39.811 39.802a4.8 4.8 0 0 1 -6.797 6.797l-43.2 -43.2a4.8 4.8 0 0 1 0 -6.797l43.2 -43.2a4.8 4.8 0 1 1 6.797 6.797L35.587 76.8z" />
    </svg>
  );
});
