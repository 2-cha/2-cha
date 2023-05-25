import Image from 'next/image';

import { Member } from '@/types';

import styles from './ProfileHeader.module.scss';

interface Props {
  member: Member;
}

export default function ProfileHeader({ member }: Props) {
  console.log(member);
  return (
    <header className={styles.root}>
      <div className={styles.topdiv}>
        {/* <Image
          src={member.prof_img}
          width={120}
          height={120}
          alt="member profile pic"
        />
		// TODO: profile pic
		*/}
        <div className={styles.image}>프로필 이미지</div>
        <div className={styles.profiledata}>
          <h1>{member.name}</h1>
          <h2>업적</h2>
          <button type="button">
            <span>팔로우</span>
          </button>
        </div>
      </div>
      <div className={styles.tagdata}>
        <h3>Top 5</h3>
        <ul className={styles.taglist}>
          <li>태그</li>
          <li>태그</li>
          <li>태그</li>
        </ul>
      </div>
    </header>
  );
}
