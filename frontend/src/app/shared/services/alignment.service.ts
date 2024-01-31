import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { optionalValue } from '../common';
import { Alignment } from '../types/model/Alignment';

@Injectable({
  providedIn: 'root',
})
export class AlignmentService {
  constructor(private http: HttpClient) {}

  getAlignments(quarterId?: number, teamIds?: number[], objectiveQuery?: string): Observable<Alignment[]> {
    const params = optionalValue({
      quarter: quarterId,
      teamIds: teamIds,
      objectiveQuery: objectiveQuery,
    });
    return this.http.get<Alignment[]>('/api/v2/alignments/filtered', { params: params });
  }
}
