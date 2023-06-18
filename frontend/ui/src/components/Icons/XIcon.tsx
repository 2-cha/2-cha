import { memo } from 'react';
import cn from 'classnames';
import s from './Icon.module.scss';

export default memo(function XIcon({
  width = 20,
  height = 20,
  className,
  ...props
}: React.ComponentProps<'svg'> & { isActive?: boolean }) {
  return (
    <svg
      width={width}
      height={height}
      className={cn(s.fill, className)}
      {...props}
      viewBox="0 0 32 32"
    >
      <g id="cross">
        <line
          fill="currentColor"
          stroke="currentColor"
          x1="7"
          x2="25"
          y1="7"
          y2="25"
        />

        <line
          fill="currentColor"
          stroke="currentColor"
          x1="7"
          x2="25"
          y1="25"
          y2="7"
        />
      </g>
    </svg>
  );
});
