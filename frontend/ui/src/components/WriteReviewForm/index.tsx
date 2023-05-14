import * as React from 'react';
import { usePlaceQuery } from '@/hooks/query/usePlace';
import { useRouter } from 'next/router';
import { FormProvider, useForm } from 'react-hook-form';
import ImagePicker from '@/components/WriteReviewForm/ImagePicker';
import TagPicker from '@/components/WriteReviewForm/TagPicker';
import PlaceIcon from '@/components/Icons/PlaceIcon';
import type { Tag } from '@/types';
import { useReviewMutation } from '@/hooks/mutation/useReview';
import s from './WriteReviewForm.module.scss';

export interface ReviewFormData {
  placeId: string;
  images: string[];
  tags: Tag[];
}

export default function WriteReviewForm() {
  const router = useRouter();
  const { placeId } = router.query;

  const method = useForm<ReviewFormData>();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = method;

  const reviewMutation = useReviewMutation();
  const onSubmit = handleSubmit(async (data) => {
    reviewMutation.mutate(
      { placeId, urls: data.images, tags: data.tags },
      {
        onSuccess: () => router.push(`/places/${placeId}`),
        onError: () => {
          /* TODO: error handling*/
          alert('리뷰 작성에 실패했습니다.');
        },
      }
    );
  });

  return (
    <FormProvider {...method}>
      <div className={s.root}>
        <form onSubmit={onSubmit} id="write" className={s.form}>
          <PlaceLabel placeId={placeId} />
          <input
            {...register('placeId', { required: true })}
            value={placeId}
            hidden
          />

          <div>
            <ImagePicker name="images" />
            {errors.images && (
              <div className={s.errorMessage}>이미지를 선택해주세요</div>
            )}
          </div>
        </form>

        <div>
          <TagPicker name="tags" />
          {errors.tags && (
            <div className={s.errorMessage}>태그를 선택해주세요</div>
          )}
        </div>

        <button type="submit" form="write" className={s.submit}>
          작성
        </button>
      </div>
    </FormProvider>
  );
}

function PlaceLabel({ placeId }: { placeId?: string | string[] }) {
  const { data, isLoading, isError } = usePlaceQuery(placeId);

  return (
    <div>
      <PlaceIcon />
      {isError || !placeId ? (
        <div>가게를 찾을 수 없어요</div>
      ) : isLoading ? (
        <div>...</div>
      ) : (
        <span>{data.name}</span>
      )}
    </div>
  );
}
