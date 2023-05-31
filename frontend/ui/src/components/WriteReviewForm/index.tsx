import { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import { useModal } from '@/hooks/useModal';
import { usePlaceQuery } from '@/hooks/query/usePlace';
import { useRecoilValue } from 'recoil';
import { suggestionsState } from '@/atoms/suggestions';
import { FormProvider, useForm, useFormContext } from 'react-hook-form';
import SearchPlaceModal from './SearchPlaceModal';
import ImagePicker from '@/components/WriteReviewForm/ImagePicker';
import TagPicker from '@/components/WriteReviewForm/TagPicker';
import PlaceIcon from '@/components/Icons/PlaceIcon';
import ImagesIcon from '@/components/Icons/ImagesIcon';
import HashIcon from '../Icons/HashIcon';
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
  const { placeId: placeIdQuery } = router.query;
  const initialPlaceId = Array.isArray(placeIdQuery)
    ? placeIdQuery[0]
    : placeIdQuery;

  const [placeId, setPlaceId] = useState<string>(initialPlaceId || '');
  useEffect(() => {
    if (initialPlaceId) {
      setPlaceId(initialPlaceId);
    }
  }, [initialPlaceId]);

  const { isOpen, onOpen, onClose } = useModal({ id: 'placePicker' });
  const suggestions = useRecoilValue(suggestionsState);

  const method = useForm<ReviewFormData>();
  const {
    handleSubmit,
    formState: { errors },
  } = method;

  const reviewMutation = useReviewMutation();
  const onSubmit = handleSubmit(
    (data) =>
      reviewMutation.mutate(
        { placeId, urls: data.images, tags: data.tags },
        {
          onSuccess: () => router.push(`/places/${placeId}`),
          onError: () => {
            /* TODO: error handling*/
            alert('리뷰 작성에 실패했습니다.');
          },
        }
      ),
    (errors) => console.log(errors)
  );

  return (
    <FormProvider {...method}>
      <div className={s.root}>
        <form onSubmit={onSubmit} id="write" className={s.form}>
          <div className={s.full}>
            <div className={s.label}>
              <ImagesIcon />
              <span>사진</span>
            </div>
            {errors.images && (
              <div className={s.errorMessage}>사진을 선택해주세요</div>
            )}
            <ImagePicker name="images" />
          </div>

          <div className={s.full}>
            <button type="button" className={s.label} onClick={onOpen}>
              {/* TODO: add styles when hover, active */}
              <PlaceIcon />
              <PlaceLabel placeId={placeId} />
            </button>
            <PlaceInput name="placeId" placeId={placeId} />
          </div>
        </form>

        <div className={s.full}>
          <div className={s.label}>
            <HashIcon />
            <span>태그</span>
          </div>
          {errors.tags && (
            <div className={s.errorMessage}>태그를 선택해주세요</div>
          )}
          <TagPicker name="tags" />
        </div>

        <button type="submit" form="write" className={s.submit}>
          작성
        </button>
      </div>
      <SearchPlaceModal
        isOpen={isOpen}
        onClose={onClose}
        onSelect={setPlaceId}
        suggestions={suggestions}
      />
    </FormProvider>
  );
}

function PlaceLabel({ placeId }: { placeId: string }) {
  const { data, isLoading, isError } = usePlaceQuery(placeId);

  return (
    <div className={s.group}>
      {isError || placeId === '' ? (
        <span>가게를 찾을 수 없어요</span>
      ) : isLoading ? (
        <span>...</span>
      ) : (
        <>
          <span>{data.name}</span>
          <span className={s.description}>{data.address}</span>
        </>
      )}
    </div>
  );
}

function PlaceInput({
  name,
  placeId,
}: {
  name: keyof ReviewFormData;
  placeId: string;
}) {
  const { register } = useFormContext<ReviewFormData>();

  return (
    <input {...register(name, { required: true })} value={placeId} hidden />
  );
}
