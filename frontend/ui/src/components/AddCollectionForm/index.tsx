import { useState } from 'react';
import { FormProvider } from 'react-hook-form';

export default function AddCollectionForm() {
  const [selectedReview, setSelectedReview] = useState<number[]>([]);
  const [collectionName, setCollectionName] = useState('');

  return <FormProvider></FormProvider>;
}
