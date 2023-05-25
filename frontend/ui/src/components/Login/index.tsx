import { GOOGLE_AUTH_URL } from '@/lib/auth';

import styles from './Login.module.scss';

export default function Login() {
  return (
    <div className={styles.root}>
      <h1>Sign in</h1>
      <a href={GOOGLE_AUTH_URL} className={styles.oauth}>
        Continue with Google
      </a>
    </div>
  );
}
