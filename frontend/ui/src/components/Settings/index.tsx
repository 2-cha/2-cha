import { useSignOutMutation, useWithdrawalMutation } from '@/hooks/mutation';
import { useRouter } from 'next/router';
import { ArrowIcon } from '../Icons';

import s from './Settings.module.scss';

interface Props {
  userID: string;
}

export default function Settings({ userID }: Props) {
  const router = useRouter();
  const signoutMutation = useSignOutMutation();
  const withdrawalMutation = useWithdrawalMutation();

  function handleClickBackButton() {
    router.back();
  }

  function handleClickLogout() {
    if (confirm('로그아웃 하시겠습니까?')) {
      signoutMutation.mutate();
    } else {
      return;
    }
  }

  function handleClickWithdrawal() {
    if (confirm('정말로 탈퇴 하시겠습니까?\n데이터는 삭제되지 않습니다!')) {
      withdrawalMutation.mutate();
    } else {
      return;
    }
  }

  return (
    <div className={s.wrapper}>
      <div className={s.wrapper__header}>
        <button
          type="button"
          onClick={handleClickBackButton}
          className={s.wrapper__header__button}
        >
          <ArrowIcon />
        </button>
        <h1 className={s.wrapper__header__elements}>프로필 설정</h1>
        <div className={s.wrapper__header__spacer} />
      </div>
      <div className={s.buttonWrapper}>
        <button type="button" onClick={handleClickLogout}>
          <span>로그아웃</span>
        </button>
        <button type="button" onClick={handleClickWithdrawal}>
          <span>회원 탈퇴</span>
        </button>
      </div>
    </div>
  );
}
