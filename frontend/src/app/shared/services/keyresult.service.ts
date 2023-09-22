import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { KeyResult } from '../types/model/KeyResult';
import { map, Observable } from 'rxjs';
import { KeyResultOrdinal } from '../types/model/KeyResultOrdinal';

@Injectable({
  providedIn: 'root',
})
export class KeyresultService {
  constructor(private httpClient: HttpClient) {}

  getFullKeyResult(keyresultId: number): Observable<KeyResult> {
    return this.httpClient.get<KeyResult>('/api/v2/keyresults/' + keyresultId).pipe(
      map((keyresult: any) => {
        const quarter = keyresult.objective.keyResultQuarterDto;
        keyresult.objective.quarter = {
          id: quarter.id,
          label: quarter.label,
          startDate: quarter.startDate,
          endDate: quarter.endDate,
        };
        console.log(keyresult);
        return keyresult;
      }),
    );
  }
}
