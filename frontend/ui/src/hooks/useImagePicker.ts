import { useSetRecoilState } from 'recoil';
import { useEffect, useRef, useState } from 'react';

import {
  useReviewImageMutation,
  type PostImageResponse,
} from '@/hooks/mutation';
import { isNonNullable } from '@/lib/type';
import { suggestionsState } from '@/atoms';

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

export function useImagePicker({ onChange }: ImagePickerOptions = {}) {
  const [images, setImages] = useState<Image[]>([]);
  const imageMutation = useReviewImageMutation();
  const imageRef = useRef<HTMLInputElement | null>(null);
  const callback = useRef<ImagePickerOptions['onChange']>();
  const setSuggestions = useSetRecoilState(suggestionsState);

  callback.current = onChange;

  // 렌더링 시마다 suggestions 초기화
  useEffect(() => {
    setSuggestions([]);
    return () => setSuggestions([]);
  }, [setSuggestions]);

  // 이미지 변경마다 onChange 호출
  useEffect(() => {
    if (callback.current) {
      callback.current(images);

      // suggestions 업데이트
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
    }
  }, [images, setSuggestions]);

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
