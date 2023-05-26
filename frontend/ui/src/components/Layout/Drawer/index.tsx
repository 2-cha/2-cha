import Modal, { Props as ModalProps } from 'react-modal';
import CloseIcon from '@/components/Icons/CloseIcon';
import cn from 'classnames';
import s from './Drawer.module.scss';

export interface DrawerProps extends ModalProps {
  children: React.ReactNode;
  className?: string;
  onClose?: () => void;
}

export default function Drawer(props: DrawerProps) {
  const { children, onClose, className } = props;

  // modal-overlay > modal > modal-content 순서로 구성됨
  return (
    <Modal
      className={{
        base: s.modal,
        afterOpen: s['modal--afterOpen'],
        beforeClose: s['modal--beforeClose'],
      }}
      overlayClassName={{
        base: s.modal__overlay,
        afterOpen: s['modal__overlay--afterOpen'],
        beforeClose: s['modal__overlay--beforeClose'],
      }}
      closeTimeoutMS={300}
      onRequestClose={onClose}
      {...props}
    >
      <div className={cn(s.modal__content, className)}>
        <div className={s.modal__header}>
          <button onClick={onClose} className={s.modal__closeButton}>
            <CloseIcon />
          </button>
        </div>
        {children}
      </div>
    </Modal>
  );
}
