import { memo } from 'react';

export default memo(function FeedIcon({
  width = 24,
  height = 24,
  className,
  ...props
}: React.ComponentProps<'svg'> & { isActive?: boolean }) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width={width}
      height={height}
      viewBox="0 0 36 36"
      {...props}
    >
      <title>{'image-gallery-line'}</title>
      <path
        d="M32.12 10H3.88A1.88 1.88 0 0 0 2 11.88v18.24A1.88 1.88 0 0 0 3.88 32h28.24A1.88 1.88 0 0 0 34 30.12V11.88A1.88 1.88 0 0 0 32.12 10ZM32 30H4V12h28Z"
        className="clr-i-outline clr-i-outline-path-1"
      />
      <path
        d="M8.56 19.45a3 3 0 1 0-3-3 3 3 0 0 0 3 3Zm0-4.6A1.6 1.6 0 1 1 7 16.45a1.6 1.6 0 0 1 1.56-1.6Z"
        className="clr-i-outline clr-i-outline-path-2"
      />
      <path
        d="m7.9 28 6-6 3.18 3.18L14.26 28h2l7.46-7.46L30 26.77v-2L24.2 19a.71.71 0 0 0-1 0l-5.16 5.16-3.67-3.66a.71.71 0 0 0-1 0L5.92 28Z"
        className="clr-i-outline clr-i-outline-path-3"
      />
      <path
        d="M30.14 3a1 1 0 0 0-1-1h-22a1 1 0 0 0-1 1v1h24Z"
        className="clr-i-outline clr-i-outline-path-4"
      />
      <path
        d="M32.12 7a1 1 0 0 0-1-1h-26a1 1 0 0 0-1 1v1h28Z"
        className="clr-i-outline clr-i-outline-path-5"
      />
      <path fill="none" d="M0 0h36v36H0z" />
    </svg>
  );
});
