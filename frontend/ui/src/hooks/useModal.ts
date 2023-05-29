import { useState, useCallback, useRef } from 'react';

interface ModalProps {
  id?: string;
  onOpen?: () => void;
  onClose?: () => void;
}

export function useModal(props: ModalProps = {}) {
  const [isOpen, setIsOpen] = useState(false);

  const handleOpen = useRef(props.onOpen);
  const handleClose = useRef(props.onClose);
  handleOpen.current = props.onOpen;
  handleClose.current = props.onClose;

  const onOpen = useCallback(() => {
    setIsOpen(true);
    handleOpen.current?.();
  }, []);

  const onClose = useCallback(() => {
    setIsOpen(false);
    handleClose.current?.();
  }, []);

  return {
    isOpen,
    onOpen,
    onClose,
  };
}
