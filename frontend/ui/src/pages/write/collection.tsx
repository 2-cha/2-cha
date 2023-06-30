import NavStackHeader from '@/components/Layout/NavStackHeader';
import MetaData from '@/components/MetaData';

export default function AddCollection() {
  const title = '컬렉션 추가';

  return (
    <>
      <MetaData title={title} />
      <NavStackHeader>{title}</NavStackHeader>
      {/* Write Collection */}
    </>
  );
}
