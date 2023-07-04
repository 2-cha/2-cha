import { memo } from 'react';
import cn from 'classnames';
import s from './Icon.module.scss';

export default memo(function EditIcon({
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
      viewBox="0 0 24 24"
    >
      <g id="Complete">
        <g id="edit">
          <g>
            <path
              d="M20,16v4a2,2,0,0,1-2,2H4a2,2,0,0,1-2-2V6A2,2,0,0,1,4,4H8"
              fill="none"
              stroke="currentColor"
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
            />

            <polygon
              fill="none"
              points="12.5 15.8 22 6.2 17.8 2 8.3 11.5 8 16 12.5 15.8"
              stroke="currentColor"
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
            />
          </g>
        </g>
      </g>
    </svg>
  );
});
