export interface Place {
  id: number;
  name: string;
  category: string;
  address: string;
  thumbnail?: string;
  distance: number;
}
export const placesMockData: Place[] = [
  {
    id: 1,
    name: '내추럴펭귄와인샵',
    address: '서울 서초구 신반포로23길 30 117호',
    category: 'WINE_BAR',
    distance: 200,
  },
  {
    id: 2,
    name: '노스티모',
    address: '서울 서초구 서초대로 125 201호',
    category: 'WINE_BAR',
    distance: 200,
    thumbnail:
      'https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Ffiy_reboot%2Fplace%2F8460AA977B6A4EDF991DC6BDAC46B01A',
  },
  {
    id: 3,
    name: '노작가의 아지트',
    address: '서울 서초구 강남대로97길 8 지하1층, 1, 2층',
    category: 'WINE_BAR',
    distance: 200,
    thumbnail:
      'https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Ffiy_reboot%2Fplace%2FFDAD264AF54C487DAC623112CD7CB3BF',
  },
  {
    id: 4,
    name: '더히든바인',
    address: '서울 서초구 논현로 21 1층',
    category: 'WINE_BAR',
    distance: 200,
  },
  {
    id: 5,
    name: '도곡옥',
    address: '서울 서초구 양재천로 143-6 1층',
    category: 'WINE_BAR',
    distance: 200,
    thumbnail:
      'https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocalfiy%2FA71F9ACA872B4849BF02D5BE94CD1FB4',
  },
  {
    id: 6,
    name: '뚜르뒤뱅',
    address: '서울 서초구 사평대로22길 55 대선빌딩 1층',
    category: 'WINE_BAR',
    distance: 200,
    thumbnail:
      'https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2F63DA209DA08C41F58264B7B847230597',
  },
  {
    id: 7,
    name: '라디치',
    address: '서울 서초구 동광로39길 79',
    category: 'WINE_BAR',
    distance: 200,
    thumbnail:
      'https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2F8B54E7BE30474437933F7BF29F827E05',
  },
  {
    id: 8,
    name: '라메종뒤땅',
    address: '서울 서초구 서초대로51길 26',
    category: 'WINE_BAR',
    distance: 200,
    thumbnail:
      'https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocalfiy%2Fsearchregister_1518718838',
  },
  {
    id: 9,
    name: '라임바',
    address: '서울 서초구 강남대로37길 12-11 1층',
    category: 'WINE_BAR',
    distance: 200,
    thumbnail:
      'https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2F471E3B804314403E975D369F547AFA0F',
  },
  {
    id: 10,
    name: '라프라스',
    address: '서울 서초구 서래로 27',
    category: 'WINE_BAR',
    distance: 200,
    thumbnail:
      'https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fcfile%2F197A4D464F5D539004',
  },
  {
    id: 11,
    name: '루베크',
    address: '서울 서초구 서초대로73길 50 4층',
    category: 'WINE_BAR',
    distance: 200,
    thumbnail:
      'https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2F0FFFE5D83B174CB1960EAE5588B98F76',
  },
  {
    id: 12,
    name: '르물랑서래',
    address: '서울 서초구 사평대로22길 17 지하1층',
    category: 'WINE_BAR',
    distance: 200,
    thumbnail:
      'https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocalfiy%2F3EF08ADF8A8C46F2888C6F57D6486E84',
  },
];
