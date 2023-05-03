import { GOOGLE_AUTH_URL } from '@/lib/auth';

export default function LogIn() {
  return (
    <div>
      <p>Sign in</p>
      <a href={GOOGLE_AUTH_URL}>Continue with Google</a>
    </div>
  );
}
