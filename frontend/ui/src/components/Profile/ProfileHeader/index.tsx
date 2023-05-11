import Image from 'next/image';

import { Member } from '@/types';

interface Props {
  member: Member;
}

export default function ProfileHeader({ member }: Props) {
  console.log(member);
  return (
    <header>
      <div>
        {/* <Image
          src={member.prof_img}
          width={120}
          height={120}
          alt="member profile pic"
        />
		// TODO: profile pic
		*/}
        <div>
          <h1>{member.name}</h1>
          <h2>업적</h2>
          <button type="button">
            <span>팔로우</span>
          </button>
        </div>
      </div>
      <div>
        <h3>Top 5</h3>
        <ul>
          <li>태그</li>
          <li>태그</li>
          <li>태그</li>
        </ul>
      </div>
    </header>
  );
}
