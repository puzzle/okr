export function getNumberOrNull(str: string | null): number | null {
  if (str === null || str.toString().trim() === '') {
    return null;
  }
  const number: number = parseInt(str, 10);
  return Number.isNaN(number) ? null : number;
}
