import cx from 'classnames';
import { Tag } from '@/types';

import styles from './ShortTags.module.scss';

interface Props {
  tagList: Tag[];
  keyID: string;
  limit?: number;
  className?: string;
}

export default function ShortTags({ tagList, keyID, className }: Props) {
  return (
    <div className={cx(className, styles.tagWrapper)}>
      {tagList.map((tag, index) => (
        <div key={`tag-ist-${keyID}-${index}`} className={styles.tag}>
          <span>
            {tag.emoji} {tag.count}
          </span>
        </div>
      ))}
    </div>
  );
}
