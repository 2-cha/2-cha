import { useCallback, useState } from 'react';
import { FormProvider, useForm } from 'react-hook-form';

import { useAuth } from '@/hooks';
import AddReviews from './AddReviews';
import DecorateCover from './DecorateCover';
import { ArrowIcon } from '../Icons';

import s from './AddCollectionForm.module.scss';

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
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [isOpen, setIsOpen] = useState(true);

  const method = useForm<CollectionFormData>();
  const {
    handleSubmit,
    formState: { errors },
  } = method;

  const handleClickModifyCover = useCallback(() => {
    setIsOpen(true);
  }, [setIsOpen]);

  return (
    <FormProvider {...method}>
      <button
        type="button"
        onClick={handleClickModifyCover}
        className={s.openCover}
      >
        <ArrowIcon />
        <span>표지 편집으로 돌아가기</span>
      </button>
      <AddReviews
        selectedReviews={selectedReview}
        setSelectedReviews={setSelectedReview}
        memberId={memberId}
      />
      <button type="submit" form="write" className={s.submit}>
        <span>작성</span>
      </button>
      <DecorateCover isOpen={isOpen} setIsOpen={setIsOpen} />
    </FormProvider>
  );
}
