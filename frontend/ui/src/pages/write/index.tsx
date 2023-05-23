import NavStackHeader from '@/components/Layout/NavStackHeader';
import MetaData from '@/components/MetaData';
import WriteReviewForm from '@/components/WriteReviewForm';

export default function WriteReview() {
  const title = '리뷰 작성';
  // TODO: require login

  return (
    <>
      <MetaData title={title} />
      <NavStackHeader>{title}</NavStackHeader>
      <WriteReviewForm />
    </>
  );
}
