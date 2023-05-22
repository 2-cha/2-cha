'use client';

import Form from '@/app/(require-auth)/tags/Form';
import Modal from '@/components/modal';

export default function EditTagModal({
  params: { id },
}: {
  params: { id: string };
}) {

  return (
    <Modal title='Edit tag'>
      <Form id={id} />
    </Modal>
  );
}
