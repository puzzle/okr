export function getNumberOrNull(str: string | null | undefined): number | null {
  if (str === null || str === undefined || str.toString().trim() === '') {
    return null;
  }
  const number: number = parseInt(str, 10);
  return Number.isNaN(number) ? null : number;
}
