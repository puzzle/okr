import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AlignmentLists } from '../types/model/AlignmentLists';
import { optionalValue } from '../common';

@Injectable({
  providedIn: 'root',
})
export class AlignmentService {
  constructor(private httpClient: HttpClient) {}

  getAlignmentByFilter(quarterId?: number, teamIds?: number[], objectiveQuery?: string): Observable<AlignmentLists> {
    const params = optionalValue({
      teamFilter: teamIds,
      quarterFilter: quarterId,
      objectiveQuery: objectiveQuery,
    });

    return this.httpClient.get<AlignmentLists>(`/api/v2/alignments/alignments`, { params: params });
  }
}
