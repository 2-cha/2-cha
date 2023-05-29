import cx from 'classnames';

import { Tag } from '@/types';

import styles from './Tags.module.scss';

interface Props {
  tagList: Tag[];
  keyID: string;
  className?: string;
  limit?: number;
}

export default function Tags({ tagList, keyID, limit, className }: Props) {
  if (limit) tagList = tagList.slice(0, limit);

  return (
    <div className={cx(styles.tags, className)}>
      {tagList.map((tag) => (
        <div key={`tag-list-${keyID}-${tag.id}`} className={styles.tag}>
          <span>
            {tag.emoji} {tag.message}
          </span>
        </div>
      ))}
    </div>
  );
}
