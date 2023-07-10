import Link from 'next/link';
import { Swiper, SwiperSlide } from 'swiper/react';
import cn from 'classnames';

import { PlusSquareIcon } from '@/components/Icons';
import { CollectionType } from '@/hooks/query';

import s from './ProfileCollection.module.scss';
import 'swiper/css';
import Image from 'next/image';

interface Props {
  collections: CollectionType[];
  isMe?: boolean;
}

export default function ProfileCollection({ collections, isMe }: Props) {
  const width = window.innerWidth;

  return (
    <div className={s.root}>
      <Swiper
        className={s.wrapper}
        spaceBetween={10}
        slidesPerView={width < 480 ? 4 : 5}
        scrollbar
      >
        {isMe && (
          <SwiperSlide>
            <Link
              href="/write/collection"
              className={cn(s.wrapper__add, s.element)}
            >
              <PlusSquareIcon withoutBorder />
            </Link>
          </SwiperSlide>
        )}
        {collections &&
          collections.map((collection) => (
            <SwiperSlide key={collection.id} className={s.wrapper__element}>
              <Link
                href={`/collections/${collection.id}`}
                className={s.element}
              >
                <Image
                  src={collection.thumbnail}
                  alt={`${collection.id} collection thumbnail`}
                  width={100}
                  height={100}
                />
              </Link>
            </SwiperSlide>
          ))}
      </Swiper>
    </div>
  );
}

// TODO: collection 완성
