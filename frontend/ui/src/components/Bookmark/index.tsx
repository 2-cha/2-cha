import Link from 'next/link';

import Tab from '@/components/Tab';
import { Grid, GridItem } from '@/components/Layout/Grid';
import { useBookmarkQuery } from '@/hooks/query/useBookmark';
import { useQueryParam } from '@/hooks/useQueryParam';

import s from './Bookmark.module.scss';

const menuItems = ['장소', '컬렉션'];

export default function Bookmark() {
  const [currentTab, setCurrentTab] = useQueryParam({
    key: 'tab',
    defaultValue: '0',
  });

  return (
    <>
      <Tab
        menuList={menuItems}
        currentIndex={currentTab}
        setCurrentIndex={setCurrentTab}
      />
      {currentTab === '0' ? (
        <BookmarkedItems type="places" />
      ) : currentTab === '1' ? (
        <BookmarkedItems type="collections" />
      ) : null}
    </>
  );
}

function BookmarkedItems({ type }: { type: string }) {
  const { data: items } = useBookmarkQuery(type);

  return (
    <>
      {items && items.length ? (
        <Grid>
          {items.map((item: any) => (
            <GridItem key={item.id}>
              <Link href={type + '/' + item.id} className={s.item__link}>
                {item.image || item.thumbnail ? (
                  <img src={item.image ?? item.thumbnail} alt={item.name} />
                ) : (
                  <div className={s.item__noImage}>
                    {item.name ?? item.title}
                  </div>
                )}
              </Link>
            </GridItem>
          ))}
        </Grid>
      ) : (
        <div className={s.item__empty}>
          <span>아직 북마크가 없어요</span>
          <Link href={`/${type}`}>탐색하러 가기</Link>
        </div>
      )}
    </>
  );
}
