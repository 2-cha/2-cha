import Bookmark from '@/components/Bookmark';
import NavStackHeader from '@/components/Layout/NavStackHeader';
import MetaData from '@/components/MetaData';
import { requireAuth } from '@/hooks';

export default requireAuth(function BookmarkPage() {
  const title = '내 북마크';

  return (
    <>
      <MetaData title={title} />
      <NavStackHeader backButton={false}>{title}</NavStackHeader>
      <Bookmark />
    </>
  );
});
