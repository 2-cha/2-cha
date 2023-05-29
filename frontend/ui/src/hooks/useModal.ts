import { useState, useCallback } from 'react';
import { useRefCallback } from './useRefCallback';

interface ModalProps {
  id?: string;
  onOpen?: () => void;
  onClose?: () => void;
}

export function useModal(props: ModalProps = {}) {
  const [isOpen, setIsOpen] = useState(false);

  const handleOpen = useRefCallback(props.onOpen);
  const handleClose = useRefCallback(props.onClose);

  const onOpen = useCallback(() => {
    setIsOpen(true);
    handleOpen?.();
  }, [handleOpen]);

  const onClose = useCallback(() => {
    setIsOpen(false);
    handleClose?.();
  }, [handleClose]);

  return {
    isOpen,
    onOpen,
    onClose,
  };
}
