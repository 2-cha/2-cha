import NavStackHeader from '@/components/Layout/NavStackHeader';
import MetaData from '@/components/MetaData';
import WriteReviewForm from '@/components/WriteReviewForm';
import { requireAuth } from '@/hooks';

export default requireAuth(function WriteReview() {
  const title = '리뷰 작성';

  return (
    <>
      <MetaData title={title} />
      <NavStackHeader>{title}</NavStackHeader>
      <WriteReviewForm />
    </>
  );
});
