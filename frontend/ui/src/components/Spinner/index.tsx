import s from './Spinner.module.scss';

interface SpinnerProps {
  size?: number;
}

export default function Spinner({ size = 24 }: SpinnerProps) {
  return <div className={s.spinner} style={{ width: size, height: size }} />;
}
