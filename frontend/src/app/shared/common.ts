export function getNumberOrNull(str: string | null | undefined): number | null {
  if (str === null || str === undefined || str.toString().trim() === '') {
    return null;
  }
  const number: number = parseInt(str, 10);
  return Number.isNaN(number) ? null : number;
}

export function getValueFromQuery(query: any): number[] {
  return Array.from([query])
    .flat(1)
    .map((id: any) => Number(id))
    .filter((id: number) => Number.isInteger(id));
}
