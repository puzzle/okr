import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
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
    return this.httpClient.get<Quarter[]>('api/v1/quarters');
  }
}
