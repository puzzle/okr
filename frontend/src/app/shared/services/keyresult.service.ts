import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { KeyResult } from '../types/model/KeyResult';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class KeyresultService {
  constructor(private httpClient: HttpClient) {}

  getFullKeyResult(keyresultId: number): Observable<KeyResult> {
    return this.httpClient.get<KeyResult>('/api/v2/keyresults/' + keyresultId).pipe(
      map((keyresult: any) => {
        keyresult.objective.quarter = keyresult.objective.keyResultQuarterDto;
        return keyresult;
      }),
    );
  }
}
