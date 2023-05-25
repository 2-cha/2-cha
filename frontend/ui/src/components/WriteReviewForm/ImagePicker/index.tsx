import { useFormContext } from 'react-hook-form';
import { useImagePicker, type Image } from '@/hooks/useImagePicker';
import { type ReviewFormData } from '@/components/WriteReviewForm';
import PlusSquareIcon from '@/components/Icons/PlusSquareIcon';
import s from './ImagePicker.module.scss';

interface ImagePickerProps {
  name: keyof ReviewFormData;
}

export default function ImagePicker({ name }: ImagePickerProps) {
  const { register, setValue } = useFormContext<ReviewFormData>();
  register(name, { required: true });

  const { ref, images, removeImage, handleChange } = useImagePicker({
    onChange: (images: Image[]) =>
      setValue(
        name,
        images.map((image) => image.url).filter(Boolean) as string[]
      ),
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
