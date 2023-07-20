import { useEffect } from 'react';
import { useRouter } from 'next/router';
import { useRecoilValue } from 'recoil';
import { FormProvider, useForm, useFormContext } from 'react-hook-form';

import { useModal, useQueryParamState, useTagPicker } from '@/hooks';
import { usePlaceQuery } from '@/hooks/query';
import { useReviewMutation } from '@/hooks/mutation';
import { suggestionsState } from '@/atoms';
import ImagePicker from '@/components/WriteReviewForm/ImagePicker';
import { PlaceIcon, ImagesIcon, HashIcon, EditIcon } from '@/components/Icons';
import type { Tag } from '@/types';

import SearchPlaceModal from './SearchPlaceModal';
import s from './WriteReviewForm.module.scss';
import SearchTagsModal from '../SearchTagsModal';
import { Tags } from '../Tags';

export interface ReviewFormData {
  placeId: string;
  images: string[];
  tags: Tag[];
}

export default function WriteReviewForm() {
  const router = useRouter();
  const [placeId, setPlaceId] = useQueryParamState('placeId');
  const {
    selected: selectedTags,
    setSelected: setSelectedTags,
    toggleSelect: toggleTagSelect,
  } = useTagPicker();

  const {
    isOpen: isPlaceModalOpen,
    onOpen: onPlaceModalOpen,
    onClose: onPlaceModalClose,
  } = useModal({ id: 'placePicker' });

  const {
    isOpen: isTagModalOpen,
    onOpen: onTagModalOpen,
    onClose: onTagModalClose,
  } = useModal({ id: 'tagPicker' });
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
          onSuccess: () => router.replace(`/places/${placeId}`),
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
        <div className={s.form__wrapper}>
          <form onSubmit={onSubmit} id="write" className={s.form}>
            <div className={s.form__group}>
              <div className={s.label}>
                <ImagesIcon width={24} height={24} />
                <span>사진</span>
              </div>
              <ImagePicker name="images" />
              {errors.images && (
                <div className={s.errorMessage}>
                  {errors.images.type === 'validate'
                    ? '10장 이하의 사진을 선택해세요'
                    : '사진을 선택해주세요'}
                </div>
              )}
            </div>
            <hr />
            <div className={s.form__group}>
              <button
                type="button"
                className={s.label}
                onClick={onPlaceModalOpen}
              >
                <div>
                  <PlaceIcon width={24} height={24} />
                </div>
                <span>장소</span>
                <div className={s.edit}>
                  <EditIcon width={16} height={16} />
                  <span>수정</span>
                </div>
              </button>
              <PlaceLabel placeId={placeId} />
              <HiddenInput name="placeId" value={placeId} required />
              {errors.placeId && (
                <div className={s.errorMessage}>장소를 선택해주세요</div>
              )}
            </div>
            <hr />
            <div className={s.form__group}>
              <button
                type="button"
                className={s.label}
                onClick={onTagModalOpen}
              >
                <HashIcon width={24} height={24} />
                <span>태그</span>
                <div className={s.edit}>
                  <EditIcon width={16} height={16} />
                  <span>수정</span>
                </div>
              </button>
              <Tags tagList={selectedTags} keyID="tags" />
              <HiddenInput
                name="tags"
                value={selectedTags}
                required
                validate={(tags) => tags.length <= 10}
              />
              {errors.tags && (
                <div className={s.errorMessage}>
                  {errors.tags.type === 'validate'
                    ? '10개 이하의 태그를 선택해주세요'
                    : '태그를 선택해주세요'}
                </div>
              )}
            </div>
          </form>
        </div>

        <button
          type="submit"
          form="write"
          className={s.submit}
          disabled={reviewMutation.isLoading}
        >
          작성
        </button>
      </div>

      <SearchPlaceModal
        isOpen={isPlaceModalOpen}
        onClose={onPlaceModalClose}
        onSelect={setPlaceId}
        suggestions={suggestions}
      />
      <SearchTagsModal
        isOpen={isTagModalOpen}
        onClose={onTagModalClose}
        selected={selectedTags}
        setSelected={setSelectedTags}
        toggleSelect={toggleTagSelect}
      />
    </FormProvider>
  );
}

function PlaceLabel({ placeId }: { placeId: string }) {
  const { data, isLoading, isError } = usePlaceQuery(placeId);

  return (
    <div className={s.placeLabel}>
      {isError || placeId === '' ? (
        <span></span>
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

function HiddenInput({
  name,
  value,
  type = 'text',
  required = false,
  validate,
}: {
  name: keyof ReviewFormData;
  value: ReviewFormData[keyof ReviewFormData];
  type?: React.HTMLInputTypeAttribute;
  required?: boolean;
  validate?: (
    value: ReviewFormData[keyof ReviewFormData],
    formValues: ReviewFormData
  ) => boolean;
}) {
  const { register, setValue } = useFormContext<ReviewFormData>();

  useEffect(() => {
    setValue(name, value);
  }, [setValue, name, value]);

  return (
    <input type={type} hidden {...register(name, { required, validate })} />
  );
}
