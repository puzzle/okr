import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface Measure {
  id: number | null;
  keyResultId: number;
  value: number;
  changeInfo: string;
  initiatives: string;
  createdBy: number;
  createdOn: Date;
  measureDate: Date;
}

@Injectable({
  providedIn: 'root',
})
export class MeasureService {
  constructor(private httpClient: HttpClient) {}

  getMeasureById(measureId: number) {
    return this.httpClient.get<Measure>('/api/v1/measures/' + measureId);
  }

  getInitMeasure(): Measure {
    return {
      id: null,
      keyResultId: 1,
      value: 0,
      changeInfo: '',
      initiatives: '',
      createdBy: 0,
      createdOn: new Date(),
      measureDate: new Date(),
    };
  }

  saveMeasure(measure: Measure, post: boolean) {
    if (post) {
      return this.httpClient.post<Measure>(`/api/v1/measures`, measure);
    } else {
      return this.httpClient.put<Measure>(
        `/api/v1/measures/` + measure.id,
        measure
      );
    }
  }
}
