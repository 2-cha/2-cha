import Link from 'next/link';
import { useRouter } from 'next/router';
import cn from 'classnames';

import PlaceIcon from '@/components/Icons/PlaceIcon';
import HashIcon from '@/components/Icons/HashIcon';
import BookmarkIcon from '@/components/Icons/BookmarkIcon';
import UserIcon from '@/components/Icons/UserIcon';
import PlusSquareIcon from '@/components/Icons/PlusSquareIcon';

import s from './NavBar.module.scss';

const sections = [
  { name: 'places', path: '/places', Icon: PlaceIcon },
  { name: 'collections', path: '/collections', Icon: HashIcon },
  { name: 'add-review', path: '/write', Icon: PlusSquareIcon },
  { name: 'bookmark', path: '/bookmark', Icon: BookmarkIcon },
  { name: 'profile', path: '/profile', Icon: UserIcon },
];

interface NavBarProps {
  currentSection: string;
}

export default function NavBar({ currentSection }: NavBarProps) {
  return (
    <nav className={s.navbar}>
      {sections.map((section) =>
        section.name === 'add-review' ? (
          <ReviewAddButton key={section.name} />
        ) : (
          <NavItem
            key={section.name}
            url={section.path}
            isActive={currentSection === section.name}
          >
            <section.Icon isActive={currentSection === section.name} />
          </NavItem>
        )
      )}
    </nav>
  );
}

interface NavItemProps {
  isActive: boolean;
  url: string;
  children: React.ReactNode;
}

function NavItem({ isActive, url, children }: NavItemProps) {
  return (
    <div className={cn(s.navbar__item, { [s.navbar__item_active]: isActive })}>
      <Link href={url} className={s.navbar__link}>
        {children}
      </Link>
    </div>
  );
}

function ReviewAddButton() {
  const {
    query: { placeId },
  } = useRouter();
  return (
    <div className={s.addButton}>
      <Link href={`/write${placeId ? `?placeId=${placeId}` : ''}`}>
        <PlusSquareIcon width={50} height={50} withoutBorder />
      </Link>
    </div>
  );
}
