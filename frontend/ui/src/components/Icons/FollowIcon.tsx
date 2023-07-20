import { memo } from 'react';

export default memo(function FollowIcon({
  width = 20,
  height = 20,
  isFilled = false,
  isFollowed,
  className,
  ...props
}: React.ComponentProps<'svg'> & {
  isFilled?: boolean;
  isFollowed: boolean;
}) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width={width}
      height={height}
      viewBox="0 0 64 64"
      className={className}
      {...props}
    >
      <path
        d="M107.151 110.359a7.156 6.998 0 0 1-7.44-6.692 7.156 6.998 0 0 1 6.826-7.29 7.156 6.998 0 0 1 7.472 6.656 7.156 6.998 0 0 1-6.79 7.323M103.88 113.28a12.901 12.1 0 0 0-12.902 12.1v3.265h31.766v-3.265a12.901 12.1 0 0 0-12.901-12.1h-4.089z"
        style={{
          fill: isFilled ? props.fill : 'none',
          fillOpacity: 1,
          stroke: 'currentcolor',
          strokeWidth: 1.96562,
          strokeLinecap: 'round',
          strokeLinejoin: 'round',
          strokeDasharray: 'none',
          strokeOpacity: 1,
        }}
        transform="matrix(1.50593 0 0 1.50593 -128.926 -137.879)"
      />
      {isFollowed ? (
        <path
          d="m118.946 110.885 4.816-6.491 1.26 1.052-4.815 6.491zm-1.522-3.438-.992 1.338 3.775 3.152.992-1.337z"
          style={{
            fill: 'none',
            stroke: '#3d934a',
            strokeWidth: 1.91295,
            strokeLinecap: 'round',
            strokeLinejoin: 'round',
            strokeOpacity: 1,
          }}
          transform="matrix(1.50593 0 0 1.50593 -128.926 -137.879)"
        />
      ) : (
        <path
          d="M120.323 112.17v-8.18h1.702v8.18zM117 107.323h8.258v1.686H117z"
          style={{
            fill: 'none',
            stroke: '#87bcf0',
            strokeWidth: 1.91295,
            strokeLinecap: 'round',
            strokeLinejoin: 'round',
            strokeOpacity: 1,
          }}
          transform="matrix(1.50593 0 0 1.50593 -128.926 -137.879)"
        />
      )}
    </svg>
  );
});
