import { useRecoilState } from 'recoil';
import { useMutation } from '@tanstack/react-query';
import { useRouter } from 'next/router';

import { tokenState, type Token } from '@/atoms';
import { fetchClient } from '@/lib/fetchClient';

async function signout(token: Token | null) {
  if (!token) throw new Error('Token is not found');
  const { data } = await fetchClient.post('/auth/signout', {
    refresh_token: token.refresh_token,
  });

  return data;
}

export function useSignOutMutation() {
  const [token, setToken] = useRecoilState(tokenState);
  const router = useRouter();

  const mutation = useMutation({
    mutationKey: ['auth', 'signout'],
    mutationFn: () => signout(token),
    onSuccess: () => {
      setToken(null);
      router.push('/');
    },
    onError: () => {
      alert('로그아웃에 실패하였습니다');
    },
  });

  return mutation;
}
