import { Measure } from '../services/measure.service';
import * as measureData from './mock-data/measure.json';

type NameOfMeasure = {
  [name: string]: Measure;
};

const measureDataObject: NameOfMeasure = measureData as any as NameOfMeasure;

export function loadMeasure(name: string): Measure {
  return overwriteObjectWithCorrectDate(measureDataObject[name]);
}

export function loadAllMeasure(loadWithString: boolean): Measure[] {
  let measureArray: Measure[] = [];
  for (let prop in measureDataObject) {
    if (loadWithString) {
      measureArray.push(measureDataObject[prop]);
      continue;
    }
    measureArray.push(overwriteObjectWithCorrectDate(measureDataObject[prop]));
  }
  return measureArray;
}

function overwriteObjectWithCorrectDate(measure: Measure): Measure {
  return Object.assign({}, measure, {
    createdOn: new Date(measure.createdOn),
    measureDate: new Date(measure.measureDate),
  });
}
