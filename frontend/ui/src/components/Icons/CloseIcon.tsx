import { memo } from 'react';
import cn from 'classnames';
import s from './Icon.module.scss';

export default memo(function CloseIcon({
  width = 20,
  height = 20,
  className,
  ...props
}: React.ComponentProps<'svg'>) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width={width}
      height={height}
      viewBox="0 0 24 24"
      className={cn(s.fill, className)}
      {...props}
    >
      <path
        fill="currentColor"
        d="M13.46 12L19 17.54V19h-1.46L12 13.46L6.46 19H5v-1.46L10.54 12L5 6.46V5h1.46L12 10.54L17.54 5H19v1.46L13.46 12Z"
      />
    </svg>
  );
});
