import { useMutation } from '@tanstack/react-query';

import { fetchClient } from '@/lib/fetchClient';

interface ProfileMutationProps {
  name: string;
  prof_msg: string;
}

async function putProfile({ name, prof_msg }: ProfileMutationProps) {
  return fetchClient.put(`/members`, {
    name,
    prof_msg,
  });
}

export function useProfileMutation(memberId?: string) {
  const mutation = useMutation({
    mutationFn: (props: ProfileMutationProps) => putProfile(props),
  });

  return mutation;
}
