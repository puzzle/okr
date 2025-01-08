import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Completed } from '../shared/types/model/completed';

@Injectable({
  providedIn: 'root'
})
export class CompletedService {
  constructor(private httpClient: HttpClient) {}

  createCompleted(completed: Completed): Observable<Completed> {
    return this.httpClient.post<Completed>('/api/v2/completed', completed);
  }

  deleteCompleted(objectiveId: number): Observable<Completed> {
    return this.httpClient.delete<Completed>('/api/v2/completed/' + objectiveId);
  }

  getCompleted(objectiveId: number): Observable<Completed> {
    return this.httpClient.get<Completed>('/api/v2/completed/' + objectiveId);
  }
}
