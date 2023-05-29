import { Tag } from '@/types';

import styles from './Tags.module.scss';

interface Props {
  tagList: Tag[];
  keyID: string;
}

export default function Tags({ tagList, keyID }: Props) {
  return (
    <div className={styles.tags}>
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
