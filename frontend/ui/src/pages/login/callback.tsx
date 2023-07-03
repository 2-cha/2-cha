import { useRouter } from 'next/router';

import { useSignInMutation } from '@/hooks/mutation';
import MetaData from '@/components/MetaData';

export default function Callback() {
  const router = useRouter();
  const { code } = router.query;
  useSignInMutation(code);

  // TODO: 로그인 중.. 홈으로 가기
  return <MetaData />;
}
