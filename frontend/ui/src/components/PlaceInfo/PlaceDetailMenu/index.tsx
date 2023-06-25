import {
  LocationIcon,
  GlobeIcon,
  ExternalLinkIcon,
  CopyIcon,
} from '@/components/Icons';
import type { Place } from '@/types';

import s from './PlaceDetailMenu.module.scss';

export default function PlaceDetail({ placeInfo }: { placeInfo: Place }) {
  return (
    <div className={s.root}>
      <DetailItem Icon={LocationIcon}>
        <div className={s.adress__content}>
          <div className={s.item__group}>
            <p className={s.address__1}>{placeInfo.address}</p>
            <CopyToClipboardButton text={placeInfo.address}>
              <CopyIcon className={s.icon__secondary} />
            </CopyToClipboardButton>
          </div>
          {placeInfo.lot_address ? (
            <p className={s.address__2}>{placeInfo.lot_address}</p>
          ) : null}
        </div>
      </DetailItem>

      {placeInfo.site ? (
        <DetailItem Icon={GlobeIcon}>
          <a className={s.site} href={placeInfo.site} target="_blank">
            <div className={s.item__group}>
              {placeInfo.site}
              <ExternalLinkIcon className={s.icon__secondary} />
            </div>
          </a>
        </DetailItem>
      ) : null}
    </div>
  );
}

function CopyToClipboardButton({
  text,
  children,
}: {
  text: string;
  children: React.ReactNode;
}) {
  const handleClick = async () => {
    if (navigator !== undefined) {
      try {
        await navigator.clipboard.writeText(text);
        // TODO: 복사한 뒤 피드백 ux
      } catch {}
    }
  };

  return (
    <button onClick={handleClick} className={s.button}>
      {children}
    </button>
  );
}

function DetailItem({
  Icon,
  children,
}: {
  Icon: React.ComponentType;
  children: React.ReactNode;
}) {
  return (
    <div className={s.item}>
      <div className={s.icon}>
        <Icon />
      </div>
      {children}
    </div>
  );
}
