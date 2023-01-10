import { Measure } from '../services/measure.service';
import * as measureData from './mock-data/measure.json';

export function loadMeasure(name: string): Measure {
  let measureDataObject: any = measureData;
  return overwriteObjectWithCorrectDate(measureDataObject[name]);
}

export function loadAllMeasure(loadWithString: boolean): any[] {
  let measureDataObject: any = measureData;
  let measureArray: any[] = [];
  for (let prop in measureDataObject) {
    if (loadWithString) {
      measureArray.push(measureDataObject[prop]);
      continue;
    }
    measureArray.push(overwriteObjectWithCorrectDate(measureDataObject[prop]));
  }
  return measureArray;
}

function overwriteObjectWithCorrectDate(measure: any): any {
  return Object.assign({}, measure, {
    createdOn: new Date(measure.createdOn),
    measureDate: new Date(measure.measureDate),
  });
}
