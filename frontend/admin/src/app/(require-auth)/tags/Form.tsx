'use client';

import { useForm } from 'react-hook-form';
import { useRouter } from 'next/navigation';
import {
  Button,
  FormControl,
  FormErrorMessage,
  FormLabel,
  Input,
} from '@chakra-ui/react';
import { type Tag } from '@/lib/api';

export default function EditTagForm({
  id,
  type = 'edit',
}: {
  id: string;
  type?: 'create' | 'edit';
}) {
  const router = useRouter();
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    setError,
  } = useForm<Tag>();

  const onSubmit = handleSubmit(
    async (data) => {
      try {
        const url = type === 'create' ? '/tags/create/api' : `/tags/edit/${id}`;
        const res = await fetch(url, {
          method: 'POST',
          body: JSON.stringify(data),
        });
        if (!res.ok) {
          throw new Error(`Failed to ${type} tag`);
        }

        router.back();
      } catch {
        setError('root', { message: `Failed to ${type} tag` });
      }
    },
    () => {
      setError('root', { message: `Failed to ${type} tag` });
    }
  );

  return (
    <form onSubmit={onSubmit}>
      <FormControl isInvalid={errors.id != null} isDisabled={type === 'create'}>
        <FormLabel htmlFor="id">id</FormLabel>
        <Input id="id" value={id} isReadOnly {...register('id')} />
        <FormErrorMessage>{errors.id && errors.id.message}</FormErrorMessage>
      </FormControl>

      <FormControl isInvalid={errors.emoji != null}>
        <FormLabel htmlFor="emoji">emoji</FormLabel>
        <Input id="emoji" {...register('emoji', { required: true })} />
        <FormErrorMessage>
          {errors.emoji && errors.emoji.message}
        </FormErrorMessage>
      </FormControl>

      <FormControl isInvalid={errors.message != null}>
        <FormLabel htmlFor="message">message</FormLabel>
        <Input id="message" {...register('message', { required: true })} />
        <FormErrorMessage>
          {errors.message && errors.message.message}
        </FormErrorMessage>
      </FormControl>

      <FormControl isInvalid={errors.category != null}>
        <FormLabel htmlFor="category">category</FormLabel>
        <Input id="category" {...register('category', { required: true })} />
        <FormErrorMessage>
          {errors.category && errors.category.message}
        </FormErrorMessage>
      </FormControl>

      <FormControl isInvalid={errors.root != null}>
        <FormErrorMessage>
          {errors.root && errors.root.message}
        </FormErrorMessage>
      </FormControl>

      <Button type="submit" isLoading={isSubmitting} my={6} colorScheme="teal">
        {type}
      </Button>
      <Button type="button" onClick={() => router.back()} my={6} ml={4}>
        cancel
      </Button>
    </form>
  );
}
