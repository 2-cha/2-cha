import { useEffect } from 'react';
import { useRouter } from 'next/router';
import { useMutation } from '@tanstack/react-query';
import { useSetRecoilState } from 'recoil';
import { tokenState, type Token } from '@/atoms/token';
import { fetchClient } from '@/lib/fetchClient';

async function signin(code: string | string[]) {
  const { data } = await fetchClient.post<Token>('/auth/openid/google/signin', {
    code,
  });

  return data;
}

export function useSignInMutation(code?: string | string[]) {
  const setToken = useSetRecoilState(tokenState);
  const router = useRouter();

  const mutation = useMutation({
    mutationKey: ['auth', 'signin'],
    mutationFn: (code: string | string[]) => signin(code),
    onSuccess: (data) => {
      setToken(data);
      router.push('/');
    },
    onError: () => {
      router.push('/login');
    },
  });

  useEffect(() => {
    if (!code) return;

    if (mutation.isIdle) {
      mutation.mutate(code);
    }
  }, [code, mutation]);
}
