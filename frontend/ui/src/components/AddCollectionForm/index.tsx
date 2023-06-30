import { useState } from 'react';
import { FormProvider, useForm } from 'react-hook-form';

import { useAuth } from '@/hooks';
import AddReviews from './AddReviews';

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
  const [isOpen, setIsOpen] = useState(false);

  const method = useForm<CollectionFormData>();
  const {
    handleSubmit,
    formState: { errors },
  } = method;

  return (
    <FormProvider {...method}>
      <AddReviews
        selectedReviews={selectedReview}
        setSelectedReviews={setSelectedReview}
        memberId={memberId}
      />
    </FormProvider>
  );
}
