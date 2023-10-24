export function getNumberOrNull(str: string | null | undefined): number | null {
  if (str === null || str === undefined || str.toString().trim() === '') {
    return null;
  }
  const number: number = parseInt(str, 10);
  return Number.isNaN(number) ? null : number;
}

export function getValueFromQuery(query: any): number[] {
  return Array.from([query])
    .flat()
    .filter((e) => e !== '')
    .map((e) => (typeof e == 'string' ? e.split(',') : e))
    .flat()
    .map((id: any) => Number(id))
    .filter((id: number) => Number.isInteger(id));
}

export function optionalValue(param: object): { [p: string]: any } {
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

export function sanitize(query: string) {
  return query.trim().toLowerCase();
}
export function optionalReplaceWithNulls(param: object): { [p: string]: any } {
  const clearObject = optionalValue(param);
  return Object.fromEntries(Object.entries(param).map(([k, v]) => [k, clearObject[k] === undefined ? null : v]));
}

export function areEqual(arr1: number[], arr2: number[]) {
  if (arr1.length !== arr2.length) return false;

  // implement custom sort if necessary
  arr1.sort((a, b) => a - b);
  arr2.sort((a, b) => a - b);

  // use normal for loop so we can return immediately if not equal
  for (let i = 0; i < arr1.length; i++) {
    if (arr1[i] !== arr2[i]) return false;
  }

  return true;
}

export function trackByFn(id: any): any {
  return id;
}
