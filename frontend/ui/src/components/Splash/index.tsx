import React, { CSSProperties } from 'react';

// Importing loader
import { CircleLoader } from 'react-spinners';
import GoogleButton from 'react-google-button';
import { GOOGLE_AUTH_URL } from '@/lib/auth';
import s from './Splash.module.scss';
// import './Splash.css';

export default function Splash() {
  const handleClick = (url: string) => {
    window.location.href = url;
  };

  // Custom css for loader
  const override: CSSProperties = {
    display: 'block',
    margin: '0 auto',
    borderColor: 'red',
  };

  return (
    <div className={s.root}>
      <CircleLoader color={'#36D7B7'} cssOverride={override} size={150} />
      <GoogleButton
        className={s.oauth}
        type={'light'}
        onClick={() => handleClick(GOOGLE_AUTH_URL)}
      ></GoogleButton>
    </div>
  );
}
