import { ShareIcon } from '../Icons';

import s from './ShareButton.module.scss';

interface Props {
  size?: number;
}

export default function ShareButton({ size = 24 }: Props) {
  return (
    <button type="button" className={s.share}>
      <ShareIcon width={size} height={size} />
    </button>
  );
}
