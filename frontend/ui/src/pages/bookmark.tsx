import Bookmark from '@/components/Bookmark';
import NavStackHeader from '@/components/Layout/NavStackHeader';
import MetaData from '@/components/MetaData';

export default function BookmarkPage() {
  const title = '내 북마크';

  return (
    <>
      <MetaData title={title} />
      <NavStackHeader backButton={false}>{title}</NavStackHeader>
      <Bookmark />
    </>
  );
}
