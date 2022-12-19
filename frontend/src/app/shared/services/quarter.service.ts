import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Objective } from './objective.service';

export interface Quarter {
  id: number;
  label: string;
}
@Injectable({
  providedIn: 'root',
})
export class QuarterService {
  constructor(private httpClient: HttpClient) {}

  public getQuarters(): Observable<Quarter[]> {
    // return this.httpClient.get<Quarter[]>('api/v1/quarters');
    return of([
      {
        id: 1,
        label: 'GJ 22/23-Q1',
      },
      {
        id: 2,
        label: 'GJ 22/23-Q2',
      },
      {
        id: 3,
        label: 'GJ 22/23-Q3',
      },
      {
        id: 4,
        label: 'GJ 22/23-Q4',
      },
      {
        id: 5,
        label: 'GJ 23/24-Q1',
      },
    ]);
  }
}
