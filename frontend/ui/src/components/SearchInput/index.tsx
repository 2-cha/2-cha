import { forwardRef } from 'react';
import SearchIcon from '@/components/Icons/SearchIcon';
import cn from 'classnames';
import s from './SearchInput.module.scss';

export default forwardRef<HTMLInputElement, React.ComponentProps<'input'>>(
  function SearchInput(props, ref) {
    const { className, ...rest } = props;

    return (
      <div className={s.container}>
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
