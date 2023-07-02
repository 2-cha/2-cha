import s from './Grid.module.scss';

interface GridProps {
  children: React.ReactNode;
}

export default function Grid({ children }: GridProps) {
  return <div className={s.grid}>{children}</div>;
}
