import { memo } from 'react';

export default memo(function Background({
  width = 210,
  height = 297,
  className,
  ...props
}: React.ComponentProps<'svg'>) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      xmlSpace="preserve"
      width={width}
      height={height}
      viewBox="0 0 210 297"
      {...props}
    >
      <path
        d="M0 0h210v297H0z"
        style={{
          fill: '#f6e4f6',
          fillOpacity: 0.896624,
          strokeWidth: 4.05688,
          strokeLinecap: 'round',
          strokeLinejoin: 'round',
        }}
      />
      <text
        xmlSpace="preserve"
        x={45.844}
        y={196.364}
        style={{
          fontStyle: 'normal',
          fontVariant: 'normal',
          fontWeight: 400,
          fontStretch: 'normal',
          fontSize: '22.5778px',
          fontFamily: 'Londrina Solid, cursive',
          fontVariantLigatures: 'normal',
          fontVariantCaps: 'normal',
          fontVariantNumeric: 'normal',
          fontVariantEastAsian: 'normal',
          fill: '#241c1c',
          fillOpacity: 0.987342,
          strokeWidth: 5.51201,
          strokeLinecap: 'round',
          strokeLinejoin: 'round',
        }}
      >
        <tspan
          x={45.844}
          y={196.364}
          style={{
            fontStyle: 'normal',
            fontVariant: 'normal',
            fontWeight: 400,
            fontStretch: 'normal',
            fontSize: '22.5778px',
            fontFamily: '&quot',
            fontVariantLigatures: 'normal',
            fontVariantCaps: 'normal',
            fontVariantNumeric: 'normal',
            fontVariantEastAsian: 'normal',
            fill: '#241c1c',
            fillOpacity: 0.987342,
            strokeWidth: 5.512,
          }}
        >
          {'w h e r e '}
          <tspan
            style={{
              fontStyle: 'normal',
              fontVariant: 'normal',
              fontWeight: 400,
              fontStretch: 'normal',
              fontSize: '25.4px',
              fontFamily: '&quot',
              fontVariantLigatures: 'normal',
              fontVariantCaps: 'normal',
              fontVariantNumeric: 'normal',
              fontVariantEastAsian: 'normal',
              fill: '#df89c3',
              fillOpacity: 0.987342,
            }}
          >
            {'2 '}
          </tspan>
          {'g o'}
        </tspan>
      </text>
      <g transform="translate(0 16.827)">
        <ellipse
          cx={105}
          cy={149.175}
          rx={12.166}
          ry={3.597}
          style={{
            fill: '#535353',
            fillOpacity: 0.882911,
            stroke: 'none',
            strokeWidth: 8.84701,
            strokeLinecap: 'round',
            strokeLinejoin: 'round',
            strokeOpacity: 1,
          }}
        />
        <path
          d="M103.176 76.517c-1.468 8.709-4.036 17.613-6.868 25.703-2.79 7.974-6.121 15.955-9.298 22.425-3.254 6.625-6.432 11.858-9.465 15.189-3.038 3.337-5.933 4.8-8.996 4.64-3.052-.162-6.192-1.946-9.3-4.944-3.16-3.047-6.025-7.081-8.747-11.293-1.955-3.026-5.68-9.095-7.108-11.471-2.093-3.483-4.27-7.28-6.231-11.969-1.984-4.741-3.757-10.463-4.52-16.197-.756-5.692-.403-10.802 1.721-12.726 2.3-2.083 5.599-.088 10.532 2.66 4.957 2.764 10.088 5.125 16.215 2.28 6.19-2.875 11.606-10.141 17.683-18.285 6.066-8.13 11.018-14.4 15.778-16.173 4.5-1.677 7.517 1.46 8.934 7.342 1.421 5.902 1.147 14.06-.33 22.819z"
          style={{
            fill: '#d2004c',
            fillOpacity: 0.990826,
            stroke: 'none',
            strokeWidth: 5.51201,
            strokeLinecap: 'round',
            strokeLinejoin: 'round',
            strokeOpacity: 1,
          }}
          transform="matrix(.97639 0 0 .57789 40.145 49.185)"
        />
        <path
          d="M80.428 68.506c-1.099-5.03-3.265-9.64-6.513-13.618-6.653-8.146-17.34-12.878-29.428-13.303v0c-12.088.425-22.775 5.157-29.428 13.303-3.249 3.977-5.415 8.587-6.514 13.618"
          style={{
            fill: 'none',
            fillOpacity: 0.990826,
            stroke: '#000',
            strokeWidth: 5.51201,
            strokeLinecap: 'round',
            strokeLinejoin: 'round',
            strokeOpacity: 1,
          }}
          transform="translate(60.487)"
        />
        <path
          d="M97.42 35.635c-1.919 2.146-2.867 3.482-3.564 4.608-.632 1.02-.88 1.597-1.819 3.01-.902 1.357-2.599 3.676-5.156 5.616-2.755 2.09-5.638 3.13-8.965 3.73-3.095.56-5.62.735-8.096 1.423-2.475.69-5.278 1.857-8.766 2.71-3.715.909-6.998 1.168-10.037.685-2.583-.41-5.599-1.582-8.393-2.382-2.902-.832-6.882-1.679-8.94-2.674-2.097-1.014-2.513-2.419.11-2.925 2.861-.552 6.583.389 11.97 1.817 5.207 1.38 9.972 2.494 15.192.518 5.297-2.005 9.884-6.646 16.089-11.844 6.364-5.332 12.049-9.029 17.472-10.721 5.083-1.587 7.143-.553 6.994.863-.152 1.455-2.378 3.653-4.09 5.566z"
          style={{
            fill: '#560924',
            fillOpacity: 0.990826,
            stroke: 'none',
            strokeWidth: 5.51201,
            strokeLinecap: 'round',
            strokeLinejoin: 'round',
            strokeOpacity: 1,
          }}
          transform="matrix(.98075 0 0 .74142 37.99 54.502)"
        />
        <path
          d="M7.746 82.065c.108 3.206.58 6.513 1.415 9.877 3.111 12.548 10.635 23.61 19.884 31.95 4.87 4.391 10.15 7.985 15.47 10.523 5.32-2.538 10.598-6.132 15.468-10.523 9.25-8.34 16.771-19.402 19.883-31.95.834-3.364 1.307-6.67 1.415-9.877"
          style={{
            fill: 'none',
            fillOpacity: 0.990826,
            stroke: '#000',
            strokeWidth: 5.51201,
            strokeLinecap: 'round',
            strokeLinejoin: 'round',
            strokeOpacity: 1,
          }}
          transform="translate(60.487)"
        />
      </g>
    </svg>
  );
});
