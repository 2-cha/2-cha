import { useMutation } from '@tanstack/react-query';
import { useRouter } from 'next/router';
import { useSetRecoilState } from 'recoil';

import { tokenState } from '@/atoms';
import { fetchClient } from '@/lib/fetchClient';

async function withdrawal() {
  const { data } = await fetchClient.delete('/members');
  return data;
}

export function useWithdrawalMutation() {
  const setToken = useSetRecoilState(tokenState);
  const router = useRouter();

  const mutation = useMutation({
    mutationKey: ['auth', 'withdrawal'],
    mutationFn: () => withdrawal(),
    onSuccess: () => {
      setToken(null);
      alert('탈퇴에 성공하였습니다.\n안녕히 가세요.');
      router.push('/');
    },
    onError: () => {
      alert('회원탈퇴에 실패하였습니다');
    },
  });

  return mutation;
}
