import cn from 'classnames';
import s from './Skeleton.module.scss';

interface SkeletonProps {
  type: 'circle' | 'rect';
  width?: string | number;
  height?: string | number;
  className?: string;
}

function Skeleton({ type, width, height, className }: SkeletonProps) {
  return (
    <div
      className={cn(s.root, className, {
        [s.circle]: type === 'circle',
      })}
      style={{ width: width ?? '100%', height: height ?? '100%' }}
    />
  );
}

export default Skeleton;
