import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Statistics } from '../shared/types/model/statistics';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EvaluationService {
  constructor(private http: HttpClient) {
  }

  getStatistics(quarterId: number, teamIds: number[]): Observable<Statistics> {
    const params = {
      quarter: quarterId,
      team: teamIds
    };
    return this.http.get<Statistics>('/api/v2/evaluation', { params: params });
  }
}
