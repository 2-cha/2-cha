import { redirect } from "next/navigation";

export default async function Layout({ children }: { children: React.ReactNode }) {
  try {
    const res = await fetch("/login/api");

    if (!res.ok) {
      throw new Error();
    }
  } catch {
    redirect("/login");
  }

  return <>{children}</>;
}
