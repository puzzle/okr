import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Quarter } from '../shared/types/model/quarter';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class QuarterService {
  constructor(private http: HttpClient) {}

  getAllQuarters(): Observable<Quarter[]> {
    return this.http
      .get<Quarter[]>('/api/v2/quarters')
      .pipe(map((quarters) => quarters.map((quarter) => new Quarter(
        quarter.id, quarter.label, quarter.startDate, quarter.endDate
      ))));
  }

  getCurrentQuarter(): Observable<Quarter> {
    return this.http.get<Quarter>('/api/v2/quarters/current');
  }
}
