import { useEffect, useState } from 'react';

type UseToggleButtonProps = {
  enabled: boolean;
  count?: number;
};

export function useToggleButton({ enabled, count = 0 }: UseToggleButtonProps) {
  const [state, setState] = useState({ enabled, count });

  function reset() {
    setState({ enabled, count });
  }

  function toggle() {
    setState((prev) => ({
      enabled: !prev.enabled,
      count: prev.enabled ? prev.count - 1 : prev.count + 1,
    }));

    return reset;
  }

  useEffect(() => {
    reset();
  }, [enabled, count]);

  return { toggle, ...state };
}
