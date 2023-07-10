import MetaData from '@/components/MetaData';
import Settings from '@/components/Settings';
import { requireAuth, useAuth } from '@/hooks';

export default requireAuth(function SettingsPage() {
  const { user } = useAuth();
  const memberId = user?.sub;

  return (
    <>
      <MetaData title="프로필 설정" />
      <Settings userID={memberId ?? ''} />
    </>
  );
});
