import s from './Error.module.scss';

interface ErrorProps {
  title: string;
  description?: string;
}

export default function Error({ title, description }: ErrorProps) {
  return (
    <div className={s.container}>
      <h1>{title}</h1>
      {description ? <h2>{description}</h2> : null}
    </div>
  );
}
