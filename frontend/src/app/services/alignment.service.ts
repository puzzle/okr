import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AlignmentLists } from '../shared/types/model/AlignmentLists';
import { optionalValue } from '../shared/common';

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

    return this.httpClient.get<AlignmentLists>(`/api/v2/alignments/alignmentLists`, { params: params });
  }
}
