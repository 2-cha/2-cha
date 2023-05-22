import Form from '@/app/(require-auth)/tags/Form';

export default function EditTagPage({
  params: { id },
}: {
  params: { id: string };
}) {
  return <Form id={id} />;
}
