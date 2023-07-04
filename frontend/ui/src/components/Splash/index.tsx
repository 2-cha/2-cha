import React, { useEffect, useState } from 'react';

// Importing loader
import GoogleButton from 'react-google-button';
import { GOOGLE_AUTH_URL } from '@/lib/auth';
import s from './Splash.module.scss';
import Background from '@/components/Splash/Background';
// import './Splash.css';

export default function Splash() {
  const handleClick = (url: string) => {
    window.location.href = url;
  };

  const [windowSize, setWindowSize] = useState([
    global?.window ? window.innerWidth : 720,
    global?.window ? window.innerHeight : 1080,
  ]);

  useEffect(() => {
    const handleWindowResize = () => {
      setWindowSize([window.innerWidth, window.innerHeight]);
    };

    window.addEventListener('resize', handleWindowResize);

    return () => {
      window.removeEventListener('resize', handleWindowResize);
    };
  }, []);

  return (
    <div className={s.root}>
      <Background width={windowSize[0]} height={windowSize[1]} />
      <GoogleButton
        className={s.oauth}
        type={'light'}
        onClick={() => handleClick(GOOGLE_AUTH_URL)}
      ></GoogleButton>
    </div>
  );
}
