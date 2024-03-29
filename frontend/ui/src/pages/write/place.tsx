import NavStackHeader from '@/components/Layout/NavStackHeader';
import MetaData from '@/components/MetaData';
import AddPlaceForm from '@/components/AddPlaceForm';
import { requireAuth } from '@/hooks';

export default requireAuth(function RegistPlace() {
  const title = '가게 등록';

  return (
    <>
      <MetaData title={title} />
      <NavStackHeader>{title}</NavStackHeader>
      <AddPlaceForm />
    </>
  );
});
