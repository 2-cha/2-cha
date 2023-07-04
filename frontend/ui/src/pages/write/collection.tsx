import AddCollectionForm from '@/components/AddCollectionForm';
import NavStackHeader from '@/components/Layout/NavStackHeader';
import MetaData from '@/components/MetaData';
import { requireAuth } from '@/hooks';

export default requireAuth(function AddCollection() {
  const title = '컬렉션 추가';

  return (
    <>
      <MetaData title={title} />
      <NavStackHeader>{title}</NavStackHeader>
      <AddCollectionForm />
    </>
  );
});
