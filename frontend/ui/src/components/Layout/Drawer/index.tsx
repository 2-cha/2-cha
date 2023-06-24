import cn from 'classnames';
import Modal, { type Props as ModalProps } from 'react-modal';

import { CloseIcon } from '@/components/Icons';

import s from './Drawer.module.scss';

export interface DrawerProps extends ModalProps {
  children: React.ReactNode;
  header?: React.ReactNode;
  className?: string;
  onClose?: () => void;
}

export default function Drawer(props: DrawerProps) {
  const { children, header, onClose, className, ...rest } = props;

  Modal.setAppElement('#__next');

  // modal-overlay > modal > modal-content 순서로 구성됨
  return (
    <Modal
      className={{
        base: cn(s.modal, className),
        afterOpen: s.modal__afterOpen,
        beforeClose: s.modal__beforeClose,
      }}
      overlayClassName={{
        base: s.modal__overlay,
        afterOpen: s.modal__overlay__afterOpen,
        beforeClose: s.modal__overlay__beforeClose,
      }}
      closeTimeoutMS={300}
      onRequestClose={onClose}
      {...rest}
    >
      <div className={s.modal__content}>
        <div className={s.modal__header}>
          <button onClick={onClose} className={s.modal__closeButton}>
            <CloseIcon />
          </button>
          {header}
        </div>
        <div className={s.modal__body}>{children}</div>
      </div>
    </Modal>
  );
}
