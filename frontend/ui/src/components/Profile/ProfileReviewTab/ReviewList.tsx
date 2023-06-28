import Image from 'next/image';
import { Fragment } from 'react';

import { Review } from '@/types';

import s from './ReviewList.module.scss';
import Link from 'next/link';

interface Props {
  pages: Review[][];
}

export default function ReviewList({ pages }: Props) {
  return (
    <ul className={s.list}>
      {pages.map((page, index) => (
        <Fragment key={index}>
          {page.map((review) => (
            <li key={review.id} className={s.list__element}>
              <Link href={`/places/${review.place.id}`}>
                <Image
                  src={review.images[0]}
                  alt={`${review.place} review`}
                  width={200}
                  height={200}
                />
              </Link>
            </li>
          ))}
        </Fragment>
      ))}
    </ul>
  );
}
