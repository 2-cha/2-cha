import { useSignInMutation } from '@/hooks/mutation/useSignin';
import { useRouter } from 'next/router';

export default function Callback() {
  const router = useRouter();
  const { code } = router.query;
  useSignInMutation(code);

  // TODO: 로그인 중.. 홈으로 가기
  return null;
}
