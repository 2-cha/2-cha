'use server';

import { cookies } from "next/headers";
import jwtDecode from "jwt-decode";

const API_URL = process.env.API_URL || "";
const TOKEN_NAME = "token";

export async function isLoggedIn() {
  const cookie = cookies().get(TOKEN_NAME);
  if (!cookie || !cookie.value) {
    return false;
  }

  const token = jwtDecode<{ sub: string }>(cookie.value);

  const res = await fetch(API_URL + `/members/${token.sub}`);
  if (!res.ok) {
    return false;
  }

  return true;
}

export async function login({
  email,
  password,
}: {
  email: string;
  password: string;
}) {
  const res = await fetch(API_URL + "/auth/signin", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });

  if (!res.ok) {
    throw new Error(res.statusText);
  }
  const { data: token } = await res.json();

  cookies().set(TOKEN_NAME, token.access_token);
  return token;
}

export async function logout() {
  cookies().set(TOKEN_NAME, "");
}
