import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { OverviewEntity } from '../types/model/OverviewEntity';

@Injectable({
  providedIn: 'root',
})
export class OverviewService {
  constructor(private http: HttpClient) {}

  getOverview(): Observable<OverviewEntity[]> {
    return this.http.get<OverviewEntity[]>('/api/v2/overview');
  }
}
