import Link from 'next/link';
import { Swiper, SwiperSlide } from 'swiper/react';
import cn from 'classnames';

import { PlusSquareIcon } from '@/components/Icons';

import s from './ProfileCollection.module.scss';
import 'swiper/css';

export default function ProfileCollection() {
  const collection = [
    '컬렉션1',
    '컬렉션2',
    '컬렉션3',
    '컬렉션4',
    '컬렉션5',
    '컬렉션6',
  ];
  const width = window.innerWidth;

  return (
    <div className={s.root}>
      <Swiper
        className={s.wrapper}
        spaceBetween={10}
        slidesPerView={width < 480 ? 4 : 5}
        scrollbar
      >
        <SwiperSlide>
          <Link
            href="/write/collection"
            className={cn(s.wrapper__add, s.element)}
          >
            <PlusSquareIcon withoutBorder />
          </Link>
        </SwiperSlide>
        {collection.map((item) => (
          <SwiperSlide key={item} className={s.wrapper__element}>
            <Link href={`/collection/${item}`} className={s.element}>
              {/* TODO: collection link */}
              {item}
              <img src={item} alt={`${item} collection thumbnail`} />
            </Link>
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
}

// TODO: collection 완성
