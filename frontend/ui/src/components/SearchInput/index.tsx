import { forwardRef } from 'react';
import cn from 'classnames';

import { SearchIcon } from '@/components/Icons';

import s from './SearchInput.module.scss';

type SearchInputProps = React.ComponentProps<'input'> & { isError?: boolean };

export default forwardRef<HTMLInputElement, SearchInputProps>(
  function SearchInput(props, ref) {
    const { className, isError, ...rest } = props;

    return (
      <div className={cn(s.container, { [s.error]: isError })}>
        <input
          ref={ref}
          type="search"
          className={cn(s.input, className)}
          {...rest}
        />
        <button type="submit" className={s.submit}>
          <SearchIcon />
        </button>
      </div>
    );
  }
);
