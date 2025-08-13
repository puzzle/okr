import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { KeyResult } from '../shared/types/model/key-result';
import { map, Observable } from 'rxjs';
import { KeyResultDto } from '../shared/types/DTOs/key-result-dto';

@Injectable({
  providedIn: 'root'
})
export class KeyResultService {
  constructor(private httpClient: HttpClient) {}

  getFullKeyResult(keyResultId: number): Observable<KeyResult> {
    return this.httpClient.get<KeyResult>('/api/v2/keyresults/' + keyResultId)
      .pipe(map((keyResult: any) => {
        keyResult.objective.quarter = keyResult.objective.keyResultQuarterDto;
        return keyResult;
      }));
  }

  saveKeyResult(keyResultDTO: KeyResultDto): Observable<KeyResult> {
    if (keyResultDTO.id) {
      return this.httpClient.put<KeyResult>('/api/v2/keyresults/' + keyResultDTO.id, keyResultDTO);
    } else {
      return this.httpClient.post<KeyResult>('/api/v2/keyresults', keyResultDTO);
    }
  }

  deleteKeyResult(keyResultId: number): Observable<any> {
    return this.httpClient.delete<void>('/api/v2/keyresults/' + keyResultId);
  }
}
