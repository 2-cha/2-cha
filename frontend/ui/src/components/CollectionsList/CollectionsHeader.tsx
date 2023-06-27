import s from './CollectionsHeader.module.scss';

export default function CollectionsHeader() {
  // const { user } = useAuth();

  return (
    <div className={s.root}>
      {/* <h1>{user?.name}님을 위한 컬렉션.</h1> */}
      <h1>유저님을 위한 컬렉션을 준비했어요.</h1>
    </div>
  );
}
