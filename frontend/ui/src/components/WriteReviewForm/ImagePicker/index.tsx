import { useFormContext } from 'react-hook-form';
import NextImage from 'next/image';
import { forwardRef, useRef } from 'react';

import { useImagePicker } from '@/hooks';
import Spinner from '@/components/Spinner';
import { PlusSquareIcon, XIcon } from '@/components/Icons';
import { mergeRefs } from '@/lib/mergeRef';

import s from './ImagePicker.module.scss';

type ImagePickerProps = ReturnType<typeof useImagePicker>;

export default forwardRef<HTMLInputElement, Omit<ImagePickerProps, 'ref'>>(
  function ImagePicker({ images, removeImage, handleChange }, ref) {
    const inputRef = useRef<HTMLInputElement>(null);

    return (
      <div className={s.root}>
        {images.map((image) => (
          <div key={image.file.name} className={s.image}>
            {image.url ? (
              <NextImage
                src={image.url}
                alt={image.file.name}
                width={240}
                height={240}
                unoptimized
              />
            ) : image.isError ? (
              <XIcon className={s.errorIcon} width={40} height={40} />
            ) : (
              <Spinner />
            )}
            <button
              className={s.image__remove}
              type="button"
              onClick={() => removeImage(image.file)}
            >
              <XIcon />
            </button>
          </div>
        ))}
        <div className={s.imageInput}>
          <input
            ref={mergeRefs(inputRef, ref)}
            type="file"
            accept="image/png, image/jpeg, image/jpg, image/gif, image/bmp, image/webp"
            multiple
            hidden
            onChange={handleChange}
          />
          <button
            className={s.addButton}
            type="button"
            onClick={() => inputRef.current?.click()}
          >
            <PlusSquareIcon width={42} height={42} />
          </button>
        </div>
      </div>
    );
  }
);
