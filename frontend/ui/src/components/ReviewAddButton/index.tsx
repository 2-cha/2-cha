import Link from 'next/link';

import PlusSquareIcon from '../Icons/PlusSquareIcon';
import s from './ReviewButton.module.scss';

export default function ReviewAddButton() {
  return (
    <div className={s.addButton}>
      <Link href="/write">
        <PlusSquareIcon width={50} height={50} withoutBorder />
      </Link>
    </div>
  );
}
