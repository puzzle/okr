import { FormGroup } from '@angular/forms';
import { KeyResultMetricMin } from './types/model/key-result-metric-min';
import { State } from './types/enums/state';
import { Action } from './types/model/action';
import { filter, map, Observable, pairwise, shareReplay } from 'rxjs';
import { Item } from '../components/action-plan/action-plan.component';

export function getNumberOrNull(str: string | null | undefined): number | null {
  if (str === null || str === undefined || str.toString()
    .trim() === '') {
    return null;
  }
  const number: number = parseInt(str, 10);
  return Number.isNaN(number) ? null : number;
}

export function getValueFromQuery(query: any, fallback?: number): number[] {
  const values = Array.from([query])
    .flat()
    .filter((e) => e !== '')
    .map((e) => {
      return typeof e == 'string' ? e.split(',') : e;
    })
    .flat()
    .map((id: any) => Number(id))
    .filter((id: number) => Number.isInteger(id));
  if (fallback === undefined) {
    return values;
  }
  return values.length > 0 ? values : [fallback];
}

export function optionalValue(param: object): Record<string, any> {
  return Object.fromEntries(Object.entries(param)
    .filter(([_,
      v]) => v != undefined)
    .filter(([_,
      v]) => v != '')
    .filter(([_,
      v]) => {
      if (Array.isArray(v)) {
        return v.length > 0;
      }
      return true;
    }));
}

export function isLastCheckInNegative(baseline: number, stretchGoal: number, value: number): boolean {
  return value > baseline && baseline > stretchGoal || value < baseline && baseline <= stretchGoal;
}

export function calculateCurrentPercentage(keyResultMetric: KeyResultMetricMin): number {
  const value: number = +keyResultMetric.lastCheckIn?.value!;
  const baseline: number = +keyResultMetric.baseline;
  const stretchGoal: number = +keyResultMetric.stretchGoal;
  if (isLastCheckInNegative(baseline, stretchGoal, value)) {
    return 0;
  }
  if (value == stretchGoal) {
    return 100;
  }

  return Math.abs(value - baseline) / Math.abs(stretchGoal - baseline) * 100;
}

export function sanitize(query: string) {
  return query.trim()
    .toLowerCase();
}

export function getQueryString(query?: string) {
  const queryString = query || '';
  return sanitize(decodeURI(queryString));
}

export function optionalReplaceWithNulls(param: object): Record<string, any> {
  const clearObject = optionalValue(param);
  return Object.fromEntries(Object.entries(param)
    .map(([k,
      v]) => [k,
      clearObject[k] === undefined ? null : v]));
}

export function areEqual(arr1: number[], arr2: number[]) {
  if (arr1.length !== arr2.length) {
    return false;
  }

  // implement custom sort if necessary
  arr1.sort((a, b) => a - b);
  arr2.sort((a, b) => a - b);

  // use normal for loop so we can return immediately if not equal
  for (let i = 0; i < arr1.length; i++) {
    if (arr1[i] !== arr2[i]) {
      return false;
    }
  }

  return true;
}

export function trackByFn(id: any): any {
  return id;
}

export function formInputCheck(form: FormGroup, propertyName: string) {
  if ((form.get(propertyName)?.dirty || form.get(propertyName)?.touched) && form.get(propertyName)?.invalid) {
    return 'dialog-form-field-error';
  } else {
    return 'dialog-form-field';
  }
}

export function isMobileDevice() {
  return window.navigator.userAgent.toLowerCase()
    .includes('mobile');
}

export function hasFormFieldErrors(formGroup: FormGroup, field: string) {
  if (formGroup.get(field)?.dirty || formGroup.get(field)?.touched) {
    return formGroup.get(field)?.errors;
  } else {
    return false;
  }
}

export function getSvgForState(objectiveState: State) {
  const svgMapping = new Map([
    ['ONGOING',
      'ongoing-icon.svg'],
    ['NOTSUCCESSFUL',
      'not-successful-icon.svg'],
    ['SUCCESSFUL',
      'successful-icon.svg'],
    ['DRAFT',
      'draft-icon.svg']
  ]);
  return svgMapping.get(objectiveState);
}

export function getValueOfForm(form: FormGroup, keys: string[]) {
  let currentControl = form;
  for (const key of keys) {
    currentControl = currentControl.get(key) as FormGroup;
  }
  return currentControl.value;
}

export function actionListToItemList(actionList: Action[]): Item[] {
  return (actionList || []).map((action) => {
    return { id: action.id,
      item: action.action,
      isChecked: action.isChecked,
      version: action.version ?? 0 } as Item;
  });
}

export function itemListToActionList(itemList: Item[], keyResultId: number | null): Action[] {
  return itemList.filter((e) => e.item.trim() !== '')
    .map((item: Item, index) => {
      return {
        id: item.id,
        action: item.item,
        priority: index,
        keyResultId: keyResultId,
        isChecked: item.isChecked,
        version: item.version
      } as Action;
    });
}


export function trackDeletedItems<T>() {
  return (source: Observable<T[]>): Observable<T[]> => {
    return source.pipe(
      pairwise(), filter(([prev,
        curr]) => prev.length > curr.length), map(([prev,
        curr]) => prev.filter((prevItem: any) => !curr.some((currentItem: any) => prevItem.id === currentItem?.id))), map((items: any[]) => items.filter((item) => item.id != null)), shareReplay()
    );
  };
}
