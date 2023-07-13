import { ChangeEvent, useCallback, useState } from 'react';
import { useRouter } from 'next/router';
import { FormProvider, useForm } from 'react-hook-form';

import { useAuth } from '@/hooks';
import {
  useCollectionImageMutation,
  useCollectionMutation,
} from '@/hooks/mutation';
import AddReviews from './AddReviews';

import s from './AddCollectionForm.module.scss';
import Image from 'next/image';
import Drawer from '../Layout/Drawer';
import { PlusSquareIcon } from '../Icons';

export interface CollectionFormData {
  title: string;
  description: string;
  thumbnail: string;
  review_ids: number[];
}

export default function AddCollectionForm() {
  const { user } = useAuth();
  const memberId = Number(user?.sub);
  const [selectedReview, setSelectedReview] = useState<number[]>([]);
  const [isOpen, setIsOpen] = useState(true);
  const [thumbnail, setThumbnail] = useState<string>('');
  const router = useRouter();

  const collectionMutation = useCollectionMutation();
  const imageMutation = useCollectionImageMutation();

  const method = useForm<CollectionFormData>();
  const {
    handleSubmit,
    register,
    formState: { errors },
  } = method;

  const onSubmit = handleSubmit(
    (data) => {
      collectionMutation.mutate(
        {
          title: data.title,
          description: data.description,
          thumbnail,
          reviewIds: selectedReview,
        },
        {
          onSuccess: () => router.replace('/profile'),
          onError: () => {
            // TODO: Error handling
            alert('컬렉션 작성에 실패하였습니다');
          },
        }
      );
    },
    (errors) => console.log(errors)
  );

  const handleClickModifyCover = useCallback(() => {
    setIsOpen(true);
  }, [setIsOpen]);

  const handleClickSelectReview = useCallback(() => {
    setIsOpen(false);
  }, [setIsOpen]);

  function handleChangeImage(e: ChangeEvent<HTMLInputElement>) {
    const newFile = e.target.files;
    if (!newFile || !newFile.item(0)) return;

    imageMutation.mutate(newFile.item(0)!, {
      onSuccess: (url) => {
        setThumbnail(url);
      },
    });
  }

  return (
    <FormProvider {...method}>
      <form onSubmit={onSubmit} id="add-collection" className={s.form}>
        <button
          type="button"
          onClick={handleClickModifyCover}
          className={s.openCover}
        >
          <span>표지 편집하기</span>
        </button>
        <AddReviews
          selectedReviews={selectedReview}
          setSelectedReviews={setSelectedReview}
          memberId={memberId}
        />
        <button type="submit" form="add-collection" className={s.submit}>
          <span>작성</span>
        </button>
        <Drawer
          isOpen={isOpen}
          onClose={() => setIsOpen(false)}
          className={s.drawer}
        >
          <div className={s.drawer__inner}>
            <div className={s.drawer__card}>
              <label
                htmlFor="collection-image-upload"
                className={s.drawer__label}
              >
                {thumbnail && thumbnail.length > 0 ? (
                  <Image
                    src={thumbnail}
                    width={360}
                    height={480}
                    unoptimized
                    alt="collection uploaded image"
                  />
                ) : (
                  <div className={s.drawer__label__noimage}>
                    <PlusSquareIcon withoutBorder />
                  </div>
                )}
              </label>
              <input
                type="file"
                id="collection-image-upload"
                hidden
                accept="image/jpeg, image/jpg, image/png"
                onChange={handleChangeImage}
              />
              <input
                type="text"
                maxLength={15}
                placeholder="컬렉션 제목을 입력해주세요"
                {...register('title', { required: true })}
                className={s.drawer__title}
              />
              <input // TODO: description 삭제
                type="text"
                hidden
                defaultValue={'test'}
                {...register('description', { required: true })}
              />
            </div>
            <button
              type="button"
              className={s.submit}
              onClick={handleClickSelectReview}
            >
              <span>리뷰 선택하기</span>
            </button>
          </div>
        </Drawer>
      </form>
    </FormProvider>
  );
}
