import Link from 'next/link';
import PlaceIcon from '@/components/Icons/PlaceIcon';
import HashIcon from '@/components/Icons/HashIcon';
import BookmarkIcon from '@/components/Icons/BookmarkIcon';
import UserIcon from '@/components/Icons/UserIcon';
import cn from 'classnames';
import s from './NavBar.module.scss';

const sections = [
  { name: 'places', path: '/places', Icon: PlaceIcon },
  { name: 'reviews', path: '/reviews', Icon: HashIcon },
  { name: 'bookmark', path: '/bookmark', Icon: BookmarkIcon },
  { name: 'profile', path: '/profile', Icon: UserIcon },
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
          <section.Icon isActive={currentSection === section.name} />
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
