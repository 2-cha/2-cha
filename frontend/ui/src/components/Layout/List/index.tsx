import cn from 'classnames';

import s from './List.module.scss';

function ListRoot(props: React.ComponentProps<'div'>) {
  const { children, className, ...rest } = props;

  return (
    <div className={cn(s.root, className)} {...rest}>
      <ul className={s.list}>{children}</ul>
    </div>
  );
}

function ListItem({
  children,
  onClick,
  selected,
}: React.PropsWithChildren<{ onClick?: () => void; selected?: boolean }>) {
  return (
    <li className={cn(s.item, { [s.selected]: selected })}>
      <button className={s.item__button} type="button" onClick={onClick}>
        {children}
      </button>
    </li>
  );
}

function ListSubheader({ children }: React.PropsWithChildren<{}>) {
  return <li className={s.subheader}>{children}</li>;
}

ListRoot.Item = ListItem;
ListRoot.Subheader = ListSubheader;

export default ListRoot;
