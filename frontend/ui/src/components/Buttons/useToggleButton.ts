import { useCallback, useEffect, useState } from 'react';

type UseToggleButtonProps = {
  enabled: boolean;
  count?: number;
};

export function useToggleButton({ enabled, count = 0 }: UseToggleButtonProps) {
  const [state, setState] = useState({ enabled, count });

  const reset = useCallback(() => {
    setState({ enabled, count });
  }, [enabled, count]);

  function toggle() {
    setState((prev) => ({
      enabled: !prev.enabled,
      count: prev.enabled ? prev.count - 1 : prev.count + 1,
    }));

    return reset;
  }

  // 초기값의 변경과 동기화
  useEffect(() => {
    reset();
  }, [reset]);

  return { toggle, ...state };
}
