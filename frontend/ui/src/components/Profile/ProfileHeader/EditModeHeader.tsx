import Image from 'next/image';
import { Dispatch, SetStateAction } from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { useQueryClient } from '@tanstack/react-query';

import { useProfileMutation } from '@/hooks/mutation/useProfile';
import { useProfileImageMutation } from '@/hooks/mutation/useProfileImage';
import { useAuth } from '@/hooks/useAuth';
import { Member } from '@/types';

import styles from './EditModeHeader.module.scss';

interface ProfileFormData {
  name: string;
  prof_msg: string;
}

interface Props {
  member: Member;
  setIsEditing: Dispatch<SetStateAction<boolean>>;
}

export default function EditModeHeader({ member, setIsEditing }: Props) {
  const method = useForm<ProfileFormData>();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = method;
  const { user } = useAuth();
  const { invalidateQueries } = useQueryClient();

  const profileMutation = useProfileMutation();
  const imageMutation = useProfileImageMutation();

  const handleSubmitProfile = handleSubmit(
    (data) =>
      profileMutation.mutate(
        { name: data.name, prof_msg: data.prof_msg },
        {
          onSuccess: () => {
            invalidateQueries(['members', user?.sub]);
            setIsEditing(false);
          },
          onError: () => {
            // TODO: error
            alert('프로필 변경에 실패하였습니다');
          },
        }
      ),
    (errors) => console.log(errors)
  );

  return (
    <FormProvider {...method}>
      <div className={styles.topDiv}>
        <div className={styles.imageWrapper}>
          <Image
            src={member.prof_img}
            width={120}
            height={120}
            alt="member profile pic"
            className={styles.image}
          />
          <div className={styles.editPhotoButtonWrapper}>
            <label htmlFor="profile-image-upload">업로드</label>
            <input
              type="file"
              id="profile-image-upload"
              accept="image/jpeg, image/jpg, image/png"
            />
          </div>
        </div>
        <div className={styles.profileData}>
          <input
            type="text"
            {...register('name', { required: true })}
            defaultValue={member.name}
            className={styles.name}
          />
          <input
            type="text"
            {...register('prof_msg', { required: true })}
            defaultValue={member.prof_msg}
            className={styles.profMsg}
          />
          <button
            type="submit"
            form="write"
            onClick={handleSubmitProfile}
            className={styles.submit}
          >
            <span>저장</span>
          </button>
        </div>
      </div>
    </FormProvider>
  );
}
