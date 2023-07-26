import { useEffect } from 'react';
import { useRouter } from 'next/router';
import { useSetRecoilState } from 'recoil';
import { useMutation } from '@tanstack/react-query';

import { type Token, tokenState } from '@/atoms';
import { fetchClient } from '@/lib/fetchClient';

async function signin(code: string) {
  const { data } = await fetchClient.post<Token>('/auth/openid/google/signin', {
    code,
  });

  return data;
}

export function useSignInMutation(code: string | undefined) {
  const setToken = useSetRecoilState(tokenState);
  const router = useRouter();

  const mutation = useMutation({
    mutationKey: ['auth', 'signin'],
    mutationFn: (code: string) => signin(code),
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
      const _code =
        process.env.NODE_ENV === 'production' ? code : code + '__dev';
      mutation.mutate(_code);
    }
  }, [code, mutation]);
}
