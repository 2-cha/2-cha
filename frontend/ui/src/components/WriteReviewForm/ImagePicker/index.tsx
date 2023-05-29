import { useEffect } from 'react';
import { useFormContext } from 'react-hook-form';
import { useSetRecoilState } from 'recoil';
import { suggestionsState } from '@/atoms/suggestions';
import { useImagePicker, type Image } from '@/hooks/useImagePicker';
import { type ReviewFormData } from '@/components/WriteReviewForm';
import { isNonNullable } from '@/lib/type';
import PlusSquareIcon from '@/components/Icons/PlusSquareIcon';
import s from './ImagePicker.module.scss';

interface ImagePickerProps {
  name: keyof ReviewFormData;
}

export default function ImagePicker({ name }: ImagePickerProps) {
  const { register, setValue } = useFormContext<ReviewFormData>();
  register(name, { required: true });

  const setSuggestions = useSetRecoilState(suggestionsState);
  useEffect(() => {
    setSuggestions([]);
    return () => setSuggestions([]);
  }, []);

  const { ref, images, removeImage, handleChange } = useImagePicker({
    onChange: (images: Image[]) => {
      setValue(
        name,
        images.map((image) => image.url).filter(Boolean) as string[]
      );

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
            <img src={image.url} alt={image.file.name} />
          ) : image.isError ? (
            <div>fail</div>
          ) : (
            <div>loading</div>
          )}
          <button
            className={s.image__remove}
            type="button"
            onClick={() => removeImage(image.file)}
          >
            x
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
