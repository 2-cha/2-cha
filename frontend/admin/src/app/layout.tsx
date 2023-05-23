import MainLayout from "./MainLayout";
import Providers from "./providers";

export const metadata = {
  title: "이차어드민",
};

async function isLogin() {
  try {
    const res = await fetch("/login/api");

    if (res.ok) {
      return true;
    }
  } catch {
    return false;
  }
}

export default async function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const isLoggedIn = await isLogin();

  return (
    <html lang="en">
      <body>
        <Providers>
          <MainLayout isLoggedIn={isLoggedIn}>{children}</MainLayout>
        </Providers>
      </body>
    </html>
  );
}
