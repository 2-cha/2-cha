import Form from '@/app/tags/Form';

export default function EditTagPage({
  params: { id },
}: {
  params: { id: string };
}) {
  return <Form id={id} />;
}
