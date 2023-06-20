import { memo } from 'react';

export default memo(function PinIcon({
  width = 32,
  height = 32,
  ...props
}: React.ComponentProps<'svg'>) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      xmlSpace="preserve"
      width={width}
      height={height}
      stroke="#000"
      strokeWidth={16.614}
      viewBox="-146.01 -146.01 795.49 795.49"
      {...props}
    >
      <path
        d="M236.733 3.267c70.827 0 128 57.173 128 128s-57.173 128-128 128-128-57.173-128-128 57.174-128 128-128"
        style={{
          fill: '#d26060',
        }}
        transform="translate(15 1)"
      />
      <path
        d="M236.733 447c61.44 0 110.933 11.093 110.933 25.6s-49.493 25.6-110.933 25.6c-61.44 0-110.933-11.093-110.933-25.6S175.293 447 236.733 447"
        style={{
          fill: '#b3dde5',
        }}
        transform="translate(15 1)"
      />
      <path
        d="M251.733 503.467c-46.08 0-115.2-7.68-115.2-29.867 0-21.333 61.44-27.307 80.213-29.013 2.56 0 4.267 1.707 4.267 4.267 0 2.56-1.707 4.267-4.267 4.267-49.493 3.413-72.533 14.507-72.533 20.48 0 7.68 36.693 21.333 106.667 21.333S357.547 481.28 357.547 473.6c0-5.973-23.04-16.213-72.533-20.48-2.56 0-4.267-2.56-4.267-4.267 0-2.56 2.56-4.267 4.267-4.267 18.773 1.707 81.067 7.68 81.067 29.013.852 22.188-68.268 29.868-114.348 29.868zm0-34.134c-2.56 0-4.267-1.707-4.267-4.267V264.533c-70.827-2.56-128-60.587-128-132.267C119.467 59.733 179.2 0 251.733 0S384 59.733 384 132.267c0 71.68-57.173 129.707-128 132.267v200.533c0 2.56-1.707 4.266-4.267 4.266zm0-460.8C183.467 8.533 128 64 128 132.267S183.467 256 251.733 256s123.733-55.467 123.733-123.733S320 8.533 251.733 8.533zm83.627 85.334c-1.707 0-3.413-.853-3.413-2.56-8.533-16.213-22.187-30.72-39.253-39.253-1.707-.853-2.56-3.413-1.707-5.973.853-1.707 3.413-2.56 5.973-1.707 17.92 9.387 33.28 24.747 42.667 42.667.853 1.707 0 4.267-1.707 5.973-1.707.853-1.707.853-2.56.853z"
        style={{
          fill: '#d26060',
        }}
      />
    </svg>
  );
});