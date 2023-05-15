import { useEffect, useRef, useState } from 'react';
import { useFormContext } from 'react-hook-form';
import { useReviewImageMutation } from '@/hooks/mutation/useReviewImage';
import { type ReviewFormData } from '@/components/WriteReviewForm';
import s from './ImagePicker.module.scss';

function isSameFile(file1: File, file2: File) {
  return (
    file1.name === file2.name &&
    file1.size === file2.size &&
    file1.lastModified === file2.lastModified
  );
}

interface ImagePickerProps {
  name: keyof ReviewFormData;
}

export interface Image {
  file: File;
  url?: string;
  isError?: boolean;
}

export default function ImagePicker({ name }: ImagePickerProps) {
  const { register, setValue } = useFormContext<ReviewFormData>();
  register(name, { required: true });

  const [images, setImages] = useState<Image[]>([]);
  const imageMutation = useReviewImageMutation();
  const imageRef = useRef<HTMLInputElement | null>(null);

  useEffect(() => {
    setValue(
      name,
      images.map((image) => image.url).filter(Boolean) as string[]
    );
  }, [images, name, setValue]);

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
            onClick={() => {
              setImages((prev) =>
                prev.filter((img) => !isSameFile(img.file, image.file))
              );
            }}
          >
            x
          </button>
        </div>
      ))}
      <div className={s.imageInput}>
        <input
          ref={imageRef}
          type="file"
          accept="image/*"
          multiple
          hidden
          onChange={(e) => {
            const newFiles = e.target.files;
            if (!newFiles) {
              return;
            }

            const newImages = Array.from(newFiles)
              .filter(
                (file) => !images.find((image) => isSameFile(image.file, file))
              )
              .map((file) => {
                imageMutation.mutate(file, {
                  onSuccess: (url) => {
                    setImages((prev) => {
                      const target = prev.find((image) =>
                        isSameFile(image.file, file)
                      );
                      if (target) {
                        target.url = url;
                      }
                      return [...prev];
                    });
                  },
                  onError: () => {
                    setImages((prev) => {
                      const target = prev.find((image) =>
                        isSameFile(image.file, file)
                      );
                      if (target) {
                        target.isError = true;
                      }
                      return [...prev];
                    });
                  },
                });
                return { file };
              });
            setImages((prev) => [...prev, ...newImages]);
          }}
        />
        <button
          className={s.addButton}
          type="button"
          onClick={() => imageRef.current?.click()}
        >
          <span>이미지 추가</span>
        </button>
      </div>
    </div>
  );
}
