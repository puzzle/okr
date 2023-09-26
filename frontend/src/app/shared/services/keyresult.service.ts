import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { KeyResult } from '../types/model/KeyResult';
import { map, Observable } from 'rxjs';
import { KeyResultDTO } from '../types/DTOs/KeyResultDTO';

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

  saveKeyResult(keyResultDTO: KeyResultDTO) {
    if (keyResultDTO.id) {
      return this.httpClient.put(`/api/v2/keyresults/` + keyResultDTO.id, keyResultDTO);
    } else {
      return this.httpClient.post('/api/v2/keyresults', keyResultDTO);
    }
  }

  deleteKeyResult(keyResultId: number) {
    return this.httpClient.delete(`/api/v2/keyresults/` + keyResultId);
  }
}
