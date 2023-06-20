import { Collection } from '@/types/collection';
import { useRouter } from 'next/router';

const MOCK_DATA: Collection = {
  id: 1,
  title: '컬렉션 이름',
  thumbnail: 'https://picsum.photos/200/500',
  member: {
    id: 54,
    name: '유저 이름',
    prof_img: 'https://picsum.photos/200/300',
    prof_msg: '유저 소개',
  },
  reviews: [
    {
      id: 1,
      images: [
        'https://picsum.photos/200/300',
        'https://picsum.photos/200/500',
      ],
      place: {
        id: 1,
        name: '장소 이름',
        category: 'WINE_BAR',
        address: '서울시 개포로 416',
        lat: 37.478922,
        lon: 127.039755,
        image: 'https://picsum.photos/200/300',
        bookmark_status: {
          is_bookmarked: false,
          count: 30,
        },
      },
      tags: [
        {
          id: 1,
          emoji: '🍷',
          message: '와인바',
          count: 40,
        },
      ],
    },
  ],
};

export default function CollectionInfoPage() {
  const { query } = useRouter();
  return <div>아아아아 {query.collectionId}</div>;
}
