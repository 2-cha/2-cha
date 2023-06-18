import { GOOGLE_AUTH_URL } from '@/lib/auth';

import s from './Login.module.scss';

export default function Login() {
  return (
    <div className={s.root}>
      <h1>Sign in</h1>
      <a href={GOOGLE_AUTH_URL} className={s.oauth}>
        Continue with Google
      </a>
    </div>
  );
}
