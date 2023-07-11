import { useRouter } from 'next/router';

import NavBar from './NavBar';

import s from './Layout.module.scss';

interface LayoutProps {
  children: React.ReactNode;
}

export default function Layout({ children }: LayoutProps) {
  const { pathname } = useRouter();
  const currentSection = pathname.split('/')[1]; // first part of path

  return (
    <div className={s.root}>
      {children}
      <NavBar currentSection={currentSection} />
    </div>
  );
}
