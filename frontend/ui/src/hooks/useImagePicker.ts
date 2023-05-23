import { useEffect, useRef, useState } from 'react';
import { useReviewImageMutation } from '@/hooks/mutation/useReviewImage';

function isSameFile(file1: File, file2: File) {
  return (
    file1.name === file2.name &&
    file1.size === file2.size &&
    file1.lastModified === file2.lastModified
  );
}

export interface Image {
  file: File;
  url?: string;
  isError?: boolean;
}

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
      .map((file) => {
        imageMutation.mutate(file, {
          onSuccess: (url) => {
            setImages((prev) => {
              const target = prev.find((image) => isSameFile(image.file, file));
              if (target) {
                target.url = url;
              }
              return [...prev];
            });
          },
          onError: () => {
            setImages((prev) => {
              const target = prev.find((image) => isSameFile(image.file, file));
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
