export function getCategoryLabel(category: string) {
  let label = '';

  switch (category) {
    case 'WINE_BAR':
      label = '와인바';
      break;
    case 'COCKTAIL_BAR':
      label = '칵테일바';
      break;
    case 'WHISKY_BAR':
      label = '위스키바';
      break;
    default:
      break;
  }

  return label;
}
