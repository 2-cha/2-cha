import { redirect } from "next/navigation";
import { cookies } from 'next/headers'

async function getUser() {
  const cookieStore = cookies();
  const token = cookieStore.get('token');

  return { token };
}

export default async function Layout({ children }: { children: React.ReactNode }) {
  const user = await getUser();
  if (user == null) {
    redirect("/login");
  }

  return <>{children}</>;
}
