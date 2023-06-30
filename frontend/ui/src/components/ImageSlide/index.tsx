import { Navigation, Pagination } from 'swiper';
import { Swiper, SwiperSlide } from 'swiper/react';
import cn from 'classnames';

import 'swiper/scss';
import 'swiper/scss/pagination';
import 'swiper/scss/navigation';
import s from './ImageSlide.module.scss';

interface ImageSlideProps {
  images: string[];
  className?: string;
  alt?: string;
}

export default function ImageSlide({
  images,
  className,
  alt,
}: ImageSlideProps) {
  return (
    <Swiper
      className={cn(s.swiper, className)}
      spaceBetween={0}
      slidesPerView={1}
      navigation={{ disabledClass: s.disabled }}
      pagination={{ clickable: true }}
      modules={[Navigation, Pagination]}
      wrapperClass={s.wrapper}
    >
      {images.map((image, index) => (
        <SwiperSlide key={index}>
          <img className={s.image} src={image} alt={`${alt}-${index}`} />
        </SwiperSlide>
      ))}
    </Swiper>
  );
}
