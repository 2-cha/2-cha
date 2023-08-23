# where2go

TODO: 이미지 높이 맞추기, 컬렉션 / 프로필 이미지 수정

  
## Place  
- 위치를 기반으로, 주변의 장소들을 나열하는 **무한 스크롤**을 제공한다.
	- 위치는 현재 좌표를 기준으로 한다.
	- 주소 검색으로 직접 지정할 수 있다.
- 리스트의 각 아이템은 **가게의 태그 요약**를 포함하여, 간단한 정보를 제공한다.
- 나열되는 장소 리스트에 대해, \[ **카테고리** | **태그**  ] 필터를 적용할 수 있다.
-  \[ **거리순**, **리뷰 많은순**, **선택한 태그가 많은순** ]으로 정렬할 수 있다.
- 장소 리스트에서 장소에 대해 북마크 / 북마크 수 확인이 가능하다.
- 북마크한 장소를 모아볼 수 있다.
- 장소 상세 페이지에서는, 장소 리뷰를 최신순으로 정렬하여 제공한다.
- 장소 상세 페이지에서는, 장소의 모든 태그 집계를 제공한다.

<img src='https://github.com/2-cha/2-cha/assets/68891716/b6c48980-5989-4279-8e13-750f16286161' alt='기본 가게 나열' width='32%'>
<img src='https://github.com/2-cha/2-cha/assets/68891716/758beecb-873f-4835-ba18-cae10c3d8557' alt='정렬 기준' width='32%'>
<img src='https://github.com/2-cha/2-cha/assets/68891716/120f5dd4-18e7-4e46-bdae-dd9082391f21' alt='위치 검색' width='32%'>

> (1) 가게 리스트  
> (2) 정렬 기준   
> (3) 위치 검색  

  
<img src='https://github.com/2-cha/2-cha/assets/68891716/1574d0da-3fb1-4fa8-b8a3-06992dce9d3d' alt='태그 필터 + 검색' width='45%'>
<img src='https://github.com/2-cha/2-cha/assets/68891716/6e51cbb8-a454-4517-a45e-1ab83292f72e' alt='태그 필터 + 태그 정렬 적용' width='45%'>

> 태그 검색 및 필터•정렬 적용

  
<img src='https://github.com/2-cha/2-cha/assets/68891716/51af3893-eaf9-4269-9410-d469a4c88fd9' alt='가게 상세 1' width='24%'>
<img src='https://github.com/2-cha/2-cha/assets/68891716/1089b1bb-2880-4fd6-a5bb-a94192e9bf05' alt='가게 상세 2' width='24%'>
<img src='https://github.com/2-cha/2-cha/assets/68891716/be2d8cc9-1074-4f16-ba0a-64d626861ccc' alt='가게 상세 3' width='24%'>
<img src='https://github.com/2-cha/2-cha/assets/68891716/3105b7dc-786b-4459-bf70-633028f9b5b1' alt='가게 상세 4' width='24%'>

> 가게 상세 페이지

  
## Review  
- Navbar UI / 장소 페이지 등에서 리뷰를 작성할 수 있다.
- 사진 업로드
	- 리뷰 사진을 업로드 할 시, 가능한 경우에 장소를 추측하여 제안한다.
- 장소 지정
	- 장소 페이지에서 접근할 경우 자동으로 채워진다.
	- 장소 검색 기능으로 직접 작성 가능하다.
		- 서비스 내에 있는 장소인 경우, 한글 초성 • Fuzzy Matching을 지원한다.
		- 서비스에 없는 장소도, 외부 장소 API 콜로 가져와 목록에 함께 노출한다.
- 태그 추가
	- 리뷰 작성 시, 사전에 정의된 태그를 사용하게 된다.
	- 태그를 검색할 수 있다.
		- 한글 초성 • Fuzzy Matching을 지원한다.
- 리뷰를 삭제할 수 있다.
- 유저가 작성한 리뷰를 모아 볼 수 있다.

<img width="30%" alt="리뷰 작성 메인" src="https://github.com/2-cha/2-cha/assets/68891716/a0479d49-b3ec-4f9e-925c-cf72c50daa08">
<img width="30%" alt="장소추천 및 검색" src="https://github.com/2-cha/2-cha/assets/68891716/f4887a98-2ed8-46aa-948f-4e99f3bc1dfa">
<img src='https://github.com/2-cha/2-cha/assets/68891716/38ebb81a-2d62-4218-aefe-8e56c8f58a5d' alt='리뷰 작성 태그 검색' width='30%'>

> (1) 리뷰 작성 메인  
> (2) 장소 검색 및 장소 제안  
> (3) 태그 추가  





## Collection  
- 작성했던 리뷰를 모아 편집물로 게시하는, **큐레이션 개념**의 포스트를 등록, 수정, 삭제할 수 있다.
- 컬렉션 리스트 조회 시, <ins>사용자의 활동 / 가까운 거리 / 최신순 기반으로 추천</ins>된다.
- 개별 컬렉션 조회 시, 컬렉션 내 리뷰•가게•작성자 정보를 확인할 수 있다.
	- 조회중인 컬렉션과 <ins>유사한 컬렉션을 추천</ins>한다.
- 각 컬렉션에 대해, 좋아요 / 좋아요 수 확인이 가능하다.
- 각 컬렉션에 대해, 북마크 / 북마크 수 확인이 가능하다.
- 북마크한 컬렉션을 따로 모아 확인할 수 있다.
- 유저가 작성한 컬렉션을 프로필에서 확인할 수 있다.

<img width="45%" alt="image" src="https://github.com/2-cha/2-cha/assets/68891716/53815eb2-668d-4a36-b7af-1eb70d95e3c5">
<img src='https://github.com/2-cha/2-cha/assets/68891716/10a64a0a-b254-417d-b52f-ae9c0967059a' alt='컬렉션 상세 및 유사 컬렉션' width='45%'>

> (1) 컬렉션 추천  
> (2) 컬렉션 상세 및 유사 컬렉션  

## Member  
- 가입 시, 랜덤한 한글 단어로 닉네임을 생성한다.
	- 사전 작성된 Word Pool의 조합으로 생성된다.
	- 중복이 발생 시, 수 회 반복하여 유니크한 닉네임을 생성한다.
	- 일정 횟수의 시도를 초과할 시 해시값을 붙인 닉네임을 생성한다.
- 닉네임•상태메세지를 변경할 수 있다.
- 프로필 이미지를 등록•변경할 수 있다.
- 탈퇴 시, Soft Delete 로 회원이 작성한 리뷰•컬렉션 등의 데이터를 유지한다.  
- 팔로우 기능


<img width="50%" alt="프로필" src="https://github.com/2-cha/2-cha/assets/68891716/0cdd8753-04b1-414e-8047-a44789796982">

> 프로필 페이지

  
## Auth  
  
- Google, Kakao OIDC 가입 / 로그인할 수 있다.
- 유저 당 최대 N개의 기기에서 접속 가능하다.  

  
## Push  
- 유저 당 최대 N개의 기기에서 푸시 알림을 받을 수 있다.
- 하나의 기기에서 다른 아이디로 로그인하거나, 
  같은 아이디로 재로그인, 다른 기기에서 로그인 등의 상황에 대비하여
  유저-토큰-토픽 간 맵핑을 관리한다.
- lastActivity를 추적하여, 토큰의 신선도를 관리한다.
- 발송 실패 토큰을 추적하여, 토큰의 유효성을 관리한다.
