import { useRouter } from 'next/router';
import { useEffect } from 'react';
import { useRecoilValue } from 'recoil';

import { jwtPayloadState } from '@/atoms';

export function useAuth() {
  const jwtPayload = useRecoilValue(jwtPayloadState);

  return { user: jwtPayload };
}

export function requireAuth<P extends {}>(Component: React.ComponentType<P>) {
  return function AuthenticateWrapper(props: P) {
    const jwtPayload = useRecoilValue(jwtPayloadState);
    const router = useRouter();

    useEffect(() => {
      if (!jwtPayload) {
        router.push('/login');
      }
    }, [jwtPayload, router]);

    return <Component {...props} />;
  };
}
