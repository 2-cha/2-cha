import Link from 'next/link';
import cn from 'classnames';
import s from './NavBar.module.scss';

const sections = [
  { name: 'places', path: '/places' },
  { name: 'reviews', path: '/reviews' },
  { name: 'search', path: '/search' },
  { name: 'profile', path: '/profile' },
];

interface NavBarProps {
  currentSection: string;
}

export default function NavBar({ currentSection }: NavBarProps) {
  return (
    <nav className={s.navbar}>
      {sections.map((section) => (
        <NavItem
          key={section.name}
          url={section.path}
          isActive={currentSection === section.name}
        >
          {section.name}
        </NavItem>
      ))}
    </nav>
  );
}

interface NavItemProps {
  isActive: boolean;
  url: string;
  children: React.ReactNode;
}

export function NavItem({ isActive, url, children }: NavItemProps) {
  return (
    <div className={cn(s.navbar__item, { [s.navbar__item_active]: isActive })}>
      <Link href={url} className={s.navbar__link}>
        {children}
      </Link>
    </div>
  );
}
