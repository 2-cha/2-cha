import { useEffect, useRef, useState } from 'react';
import {
  useReviewImageMutation,
  type PostImageResponse,
} from '@/hooks/mutation/useReviewImage';

function isSameFile(file1: File, file2: File) {
  return (
    file1.name === file2.name &&
    file1.size === file2.size &&
    file1.lastModified === file2.lastModified
  );
}

export type Image = {
  file: File;
  isError?: boolean;
} & Partial<PostImageResponse>;

export interface ImagePickerOptions {
  onChange?: (images: Image[]) => void;
}

export function useImagePicker({ onChange }: ImagePickerOptions) {
  const [images, setImages] = useState<Image[]>([]);
  const imageMutation = useReviewImageMutation();
  const imageRef = useRef<HTMLInputElement | null>(null);
  const callback = useRef<ImagePickerOptions['onChange']>();

  callback.current = onChange;

  useEffect(() => {
    if (callback.current) {
      callback.current(images);
    }
  }, [images]);

  function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
    const newFiles = e.target.files;
    if (!newFiles) {
      return;
    }

    const newImages = Array.from(newFiles)
      .filter((file) => !images.find((image) => isSameFile(image.file, file)))
      .map((file) => ({ file }));
    setImages((prev) => [...prev, ...newImages]);

    newImages.forEach(({ file }) =>
      imageMutation
        .mutateAsync(file)
        .then((data) => {
          setImages((prev) => {
            const target = prev.find((image) => isSameFile(image.file, file));

            if (target) {
              target.url = data.url;
              target.suggestions = data.suggestions;
            }
            return [...prev];
          });
        })
        .catch(() => {
          setImages((prev) => {
            const target = prev.find((image) => isSameFile(image.file, file));

            if (target) {
              target.isError = true;
            }
            return [...prev];
          });
        })
    );

    e.target.value = '';
  }

  function removeImage(file: File) {
    setImages((prev) => prev.filter((image) => !isSameFile(image.file, file)));
  }

  return {
    ref: imageRef,
    images,
    handleChange,
    removeImage,
  };
}
