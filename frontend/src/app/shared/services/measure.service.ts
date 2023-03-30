import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { isFakeMousedownFromScreenReader } from '@angular/cdk/a11y';

export interface Measure {
  id: number | null;
  keyResultId: number;
  value: number | boolean;
  changeInfo: string;
  initiatives: string;
  createdById: number | null;
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
      createdById: null,
      createdOn: new Date(),
      measureDate: new Date(),
    };
  }

  deleteMeasureById(measureId: number): Observable<Measure> {
    return this.httpClient.delete<Measure>('/api/v1/measures/' + measureId);
  }

  saveMeasure(measure: Measure, post: boolean) {
    if (post) {
      measure.createdOn = new Date();
      return this.httpClient.post<Measure>(`/api/v1/measures`, measure);
    } else {
      return this.httpClient.put<Measure>(`/api/v1/measures/` + measure.id, measure);
    }
  }
}
