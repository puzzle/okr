import { Measure } from '../services/measure.service';
import * as measureData from './mock-data/measure.json';

export function loadMeasure(name: string): Measure {
  let measureDataObject: any = measureData;
  return Object.assign({}, measureDataObject[name], {
    createdOn: turnToUTCDate(new Date(measureDataObject[name].createdOn)),
    measureDate: turnToUTCDate(new Date(measureDataObject[name].measureDate)),
  });
}

function turnToUTCDate(date: Date): Date {
  return new Date(
    date.getUTCFullYear(),
    date.getUTCMonth(),
    date.getUTCDate(),
    date.getUTCHours(),
    date.getUTCMinutes(),
    date.getUTCSeconds()
  );
}
