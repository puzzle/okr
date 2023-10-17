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

export function optional(param: object): {} {
  return Object.fromEntries(
    Object.entries(param)
      .filter(([_, v]) => v != undefined)
      .filter(([_, v]) => v != '')
      .filter(([_, v]) => {
        if (Array.isArray(v)) {
          return v.length > 0;
        }
        return true;
      }),
  );
}
