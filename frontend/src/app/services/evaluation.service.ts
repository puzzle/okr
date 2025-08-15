import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Statistics } from '../shared/types/model/statistics';
import { Observable } from 'rxjs';
import { optionalValue } from '../shared/common';

@Injectable({
  providedIn: 'root'
})
export class EvaluationService {
  private readonly http = inject(HttpClient);


  getStatistics(quarterId: number, teamIds: number[]): Observable<Statistics> {
    const params = optionalValue({
      quarter: quarterId,
      team: teamIds
    });
    return this.http.get<Statistics>('/api/v2/evaluation', { params: params });
  }
}
