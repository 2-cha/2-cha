import cn from 'classnames';

import s from './Grid.module.scss';

interface GridItemProps {
  children: React.ReactNode;
  className?: string;
}

export default function GridItem({ children, className }: GridItemProps) {
  return <div className={cn(s.gridItem, className)}>{children}</div>;
}
