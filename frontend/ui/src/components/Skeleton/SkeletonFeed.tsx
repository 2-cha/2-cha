import cn from 'classnames';

import s from './Skeleton.module.scss';

export default function SkeletonPost() {
  return (
    <div className={cn(s.wrapper, s.flexColumn)}>
      <div className={s.flexRow}>
        <div
          className={cn(s.root, s.circle)}
          style={{ width: 42, height: 42 }}
        />
        <div className={s.flexColumn}>
          <div className={s.root} style={{ width: '80%', height: '1.2rem' }} />
          <div className={s.root} style={{ width: '60%', height: '1rem' }} />
        </div>
      </div>
      <div className={s.root} style={{ width: '100%', height: 360 }} />
      <div className={s.root} style={{ width: '100%', height: 48 }} />
    </div>
  );
}
