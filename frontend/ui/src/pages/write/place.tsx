import NavStackHeader from '@/components/Layout/NavStackHeader';
import MetaData from '@/components/MetaData';
import RegisterPlaceForm from '@/components/RegistPlaceForm';

export default function RegistPlace() {
  const title = '가게 등록';

  return (
    <>
      <MetaData title={title} />
      <NavStackHeader>{title}</NavStackHeader>
      <RegisterPlaceForm />
    </>
  );
}
