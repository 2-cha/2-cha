import { GOOGLE_AUTH_URL } from '@/lib/auth';
import Image from 'next/image';

import s from './Login.module.scss';

export default function Login() {
  return (
    <div className={s.root}>
      <h1>Sign in</h1>
      <a href={GOOGLE_AUTH_URL} className={s.oauth}>
        <Image
          src="/google_signin.png"
          width={400}
          height={100}
          unoptimized
          alt="google logo"
        />
      </a>
    </div>
  );
}
