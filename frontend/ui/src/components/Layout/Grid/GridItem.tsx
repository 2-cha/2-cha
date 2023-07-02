import s from './Grid.module.scss';

interface GridItemProps {
  children: React.ReactNode;
}

export default function GridItem({ children }: GridItemProps) {
  return <div className={s.gridItem}>{children}</div>;
}
