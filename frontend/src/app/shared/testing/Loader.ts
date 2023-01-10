import { Measure } from '../services/measure.service';
import * as measureData from './mock-data/measure.json';

export function loadMeasure(name: string): Measure {
  let measureDataObject: any = measureData;
  return Object.assign({}, measureDataObject[name], {
    createdOn: new Date(measureDataObject[name].createdOn),
    measureDate: new Date(measureDataObject[name].measureDate),
  });
}
