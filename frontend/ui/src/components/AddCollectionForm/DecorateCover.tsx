import Image from 'next/image';
import { Dispatch, SetStateAction, useCallback } from 'react';

import Drawer from '../Layout/Drawer';

import s from './DecorateCover.module.scss';

interface Props {
  isOpen?: boolean;
  setIsOpen: Dispatch<SetStateAction<boolean>>;
  title: string;
  setTitle: Dispatch<SetStateAction<string>>;
  description: string;
  setDescription: Dispatch<SetStateAction<string>>;
  imageSrc: string;
  setImageSrc: Dispatch<SetStateAction<string>>;
}

export default function DecorateCover({
  isOpen = false,
  setIsOpen,
  title,
  setTitle,
  description,
  setDescription,
  imageSrc,
  setImageSrc,
}: Props) {
  const handleClickClose = useCallback(() => {
    setIsOpen(false);
  }, [setIsOpen]);

  return (
    <Drawer isOpen={isOpen} onClose={() => setIsOpen(false)}>
      <div className={s.imageWrapper}>
        {imageSrc && imageSrc.length > 0 ? (
          <Image
            src={imageSrc}
            width={360}
            height={480}
            alt="collection uploaded image"
          />
        ) : (
          <div />
        )}
        <input
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
        <input
          type="text"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
      </div>
      <button type="button">
        <span>리뷰 선택하기</span>
      </button>
    </Drawer>
  );
}
