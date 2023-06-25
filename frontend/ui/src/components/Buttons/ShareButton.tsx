import { useEffect, useState } from 'react';
import cn from 'classnames';

import { ShareIcon } from '../Icons';

import s from './ShareButton.module.scss';

interface Props {
  sharedTitle: string;
  sharedUrl: string;
  sharedText?: string;
  textAlign?: 'left' | 'right';
  size?: number;
}

const DEFAULT = 0;
const IS_SHARED = 1;
const IS_COPIED = 2;

export default function ShareButton({
  sharedTitle,
  sharedUrl,
  sharedText,
  textAlign = 'left',
  size = 24,
}: Props) {
  const [shareStatus, setShareStatus] = useState(DEFAULT);

  useEffect(() => {
    if (shareStatus === IS_SHARED || shareStatus === IS_COPIED) {
      setTimeout(() => {
        setShareStatus(DEFAULT);
      }, 1000);
    }
  }, [shareStatus]);

  function handleClickShare() {
    if (shareStatus === IS_SHARED || shareStatus === IS_COPIED) return;
    if (navigator.share) {
      navigator
        .share({
          title: sharedTitle,
          text: sharedText,
          url: sharedUrl,
        })
        .then(() => {
          setShareStatus(IS_SHARED);
        })
        .catch(console.error);
    } else {
      navigator.clipboard.writeText(sharedUrl).then(() => {
        setShareStatus(IS_COPIED);
      });
    }
  }

  return (
    <button type="button" className={s.share} onClick={handleClickShare}>
      {textAlign === 'left' && (
        <span
          className={cn(s.share__text, s.share__left, {
            [s.hidden]: shareStatus === DEFAULT,
          })}
        >
          {shareStatus === IS_SHARED && '공유 완료'}
          {shareStatus === IS_COPIED && '복사 완료'}
        </span>
      )}
      <ShareIcon width={size} height={size} />
      {textAlign === 'right' && (
        <span
          className={cn(s.share__text, s.share__right, {
            [s.hidden]: shareStatus === DEFAULT,
          })}
        >
          {shareStatus === IS_SHARED && '공유 완료'}
          {shareStatus === IS_COPIED && '복사 완료'}
        </span>
      )}
    </button>
  );
}
