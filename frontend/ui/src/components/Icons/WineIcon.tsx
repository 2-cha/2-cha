import * as React from 'react';
import { memo } from 'react';

export default memo(function WineIcon({
  width = 48,
  height = 48,
  ...props
}: React.ComponentProps<'svg'>) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width={width}
      height={height}
      viewBox="0 0 128 128"
      {...props}
    >
      <g transform="translate(-14.795 -81.43)">
        <path
          d="M53.367 123.149h50.856ZM66.945 123.135c-.903 8.54 1.58 17.392 6.79 24.22a34.228 34.228 0 0 0 14.487 10.977"
          style={{
            fill: 'none',
            stroke: 'none',
            strokeWidth: '.264583px',
            strokeLinecap: 'butt',
            strokeLinejoin: 'miter',
            strokeOpacity: 1,
          }}
        />
        <path
          d="M76.564 160.075c-5.236-.584-9.863-2.726-13.525-6.262-2.6-2.51-4.298-5.114-5.4-8.28-.912-2.618-1.105-4.266-1.104-9.424 0-4.044.142-6.611.57-10.38.126-1.112.23-2.113.23-2.223 0-.19.269-.202 4.667-.202h4.667l-.083.312c-.165.621-.08 5.569.12 7.101.95 7.232 3.762 13.433 8.55 18.854 2.778 3.147 7.014 6.278 10.961 8.104l1.1.508-.444.227c-.708.363-2.981 1.067-4.279 1.325-2.22.441-4.146.55-6.03.34z"
          style={{
            fill: '#25042d',
            fillOpacity: 0.880734,
            stroke: 'none',
            strokeWidth: 0.585441,
          }}
        />
        <path
          d="M86.117 157.856c-3.74-1.846-6.649-3.905-9.49-6.716a33.98 33.98 0 0 1-3.687-4.308c-3.177-4.412-5.263-9.567-6.176-15.302-.217-1.362-.373-6.227-.24-7.457l.084-.769h16.754c9.214 0 16.753.04 16.754.087.002.144.323 2.551.515 3.853.244 1.66.401 4.131.486 7.646.084 3.525-.069 6.137-.47 8.036-1.363 6.44-5.447 11.952-11.29 15.238-.58.326-1.136.593-1.236.593-.1 0-1.002-.406-2.004-.9z"
          style={{
            fill: '#3e0851',
            fillOpacity: 0.825688,
            stroke: 'none',
            strokeWidth: 0.602279,
          }}
        />
        <g
          style={{
            fill: '#000',
            fillOpacity: 1,
          }}
        >
          <path
            d="M96.357 100.955c-1.833.04-3.736.059-5.824.066-4.931.016-8.513-.035-12.24.062-.34.01-.641.307-.68.664 0 0-.742 6-1.895 13.498-1.309 8.515-2.171 15.361-1.839 22.379-.324 12.25 10.266 22.142 22.56 22.314 12.478-.174 22.54-10.343 22.54-22.862 0-4.073.035-8.632-.69-12.405-.332-2.958-.778-6.06-1.295-9.426-1.153-7.499-1.896-13.498-1.896-13.498-.038-.357-.339-.655-.678-.664-3.728-.097-7.31-.046-12.24-.062-2.089-.007-3.99-.027-5.823-.066z"
            style={{
              fill: 'none',
              fillOpacity: 1,
              stroke: '#000',
              strokeWidth: 3.965,
              strokeDasharray: 'none',
            }}
            transform="translate(-17.598 .99)"
          />
        </g>
        <path
          d="M62.984 187.979c-2.095 0-4.18.35-5.45.917-1.245.557-1.81 1.366-1.81 2.225s.565 1.67 1.81 2.227c1.27.568 3.355.918 5.45.918a729.232 729.232 0 0 0 4.272.004h2.394c1.082-.001 1.875-.004 1.875-.004 2.096 0 4.18-.35 5.452-.918 1.244-.557 1.808-1.368 1.808-2.227 0-.859-.564-1.668-1.808-2.225-1.272-.568-3.356-.917-5.452-.917-.791.005-2.04.003-3.195.002H66.18c-1.155 0-2.404.003-3.196-.002z"
          className="UnoptimicedTransforms"
          style={{
            fill: 'none',
            fillOpacity: 0.473214,
            stroke: '#000',
            strokeWidth: 3.3333,
            strokeLinecap: 'round',
            strokeLinejoin: 'round',
            strokeDasharray: 'none',
            strokeOpacity: 1,
          }}
          transform="matrix(1.22906 0 0 .77944 -3.865 38.178)"
        />
        <rect
          width={1.746}
          height={23.232}
          x={77.922}
          y={160.954}
          ry={0}
          style={{
            fill: '#000',
            fillOpacity: 1,
            stroke: '#000',
            strokeWidth: 2.46867,
            strokeLinecap: 'round',
            strokeLinejoin: 'round',
            strokeDasharray: 'none',
            strokeOpacity: 1,
          }}
        />
      </g>
    </svg>
  );
});
