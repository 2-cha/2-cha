import { useEffect } from 'react';
import { useFormContext } from 'react-hook-form';
import { useSetRecoilState } from 'recoil';
import NextImage from 'next/image';

import { suggestionsState } from '@/atoms';
import { isNonNullable } from '@/lib/type';
import { useImagePicker, type Image } from '@/hooks/useImagePicker';
import Spinner from '@/components/Spinner';
import { PlusSquareIcon, XIcon } from '@/components/Icons';
import { type ReviewFormData } from '@/components/WriteReviewForm';

import s from './ImagePicker.module.scss';

interface ImagePickerProps {
  name: keyof ReviewFormData;
}

export default function ImagePicker({ name }: ImagePickerProps) {
  const { register, setValue } = useFormContext<ReviewFormData>();
  register(name, { required: true, validate: (images) => images.length <= 10 });

  const setSuggestions = useSetRecoilState(suggestionsState);
  useEffect(() => {
    setSuggestions([]);
    return () => setSuggestions([]);
  }, [setSuggestions]);

  const { ref, images, removeImage, handleChange } = useImagePicker({
    onChange: (images: Image[]) => {
      setValue(name, images.map((image) => image.url).filter(isNonNullable));

      const suggestions = images
        .map((image) => image.suggestions)
        .flat()
        .filter(isNonNullable)
        .sort((a, b) => a.distance - b.distance)
        .filter(
          (sug, idx, arr) => arr.findIndex((s) => s.id === sug.id) === idx
        )
        .slice(0, 5);
      setSuggestions(suggestions);
    },
  });

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
          ref={ref}
          type="file"
          accept="image/png, image/jpeg, image/jpg, image/gif, image/bmp, image/webp"
          multiple
          hidden
          onChange={handleChange}
        />
        <button
          className={s.addButton}
          type="button"
          onClick={() => ref.current?.click()}
        >
          <PlusSquareIcon width={42} height={42} />
        </button>
      </div>
    </div>
  );
}
