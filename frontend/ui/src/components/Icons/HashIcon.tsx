import { memo } from 'react';
import cn from 'classnames';
import s from './Icon.module.scss';

export default memo(function HashIcon({
  width = 24,
  height = 24,
  className,
  isActive,
  ...props
}: React.ComponentProps<'svg'> & { isActive?: boolean }) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width={width}
      height={height}
      viewBox="0 0 16 16"
      className={cn(s.fill, className, { [s.stroke]: isActive })}
      {...props}
    >
      <path
        fill="currentColor"
        d="M8.39 12.648a1.32 1.32 0 0 0-.015.18c0 .305.21.508.5.508c.266 0 .492-.172.555-.477l.554-2.703h1.204c.421 0 .617-.234.617-.547c0-.312-.188-.53-.617-.53h-.985l.516-2.524h1.265c.43 0 .618-.227.618-.547c0-.313-.188-.524-.618-.524h-1.046l.476-2.304a1.06 1.06 0 0 0 .016-.164a.51.51 0 0 0-.516-.516a.54.54 0 0 0-.539.43l-.523 2.554H7.617l.477-2.304c.008-.04.015-.118.015-.164a.512.512 0 0 0-.523-.516a.539.539 0 0 0-.531.43L6.53 5.484H5.414c-.43 0-.617.22-.617.532c0 .312.187.539.617.539h.906l-.515 2.523H4.609c-.421 0-.609.219-.609.531c0 .313.188.547.61.547h.976l-.516 2.492c-.008.04-.015.125-.015.18c0 .305.21.508.5.508c.265 0 .492-.172.554-.477l.555-2.703h2.242l-.515 2.492zm-1-6.109h2.266l-.515 2.563H6.859l.532-2.563z"
        style={{
          strokeWidth: 0.6,
        }}
      />
    </svg>
  );
});
