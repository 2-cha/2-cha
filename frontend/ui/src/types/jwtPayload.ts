import { type JwtPayload as DefaultJwtPayload } from 'jwt-decode';

export interface JwtPayload extends DefaultJwtPayload {
  sub: string;
  name: string;
  email: string;
  role: string;
}
