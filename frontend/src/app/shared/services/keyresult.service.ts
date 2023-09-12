import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { KeyResult } from '../types/model/KeyResult'

@Injectable({
  providedIn: 'root',
})
export class KeyresultService {
  constructor(private httpClient: HttpClient) {}

  getFullKeyResult(keyresultId: number) {
    return this.httpClient.get<KeyResult>('/api/v2/keyresults/' + keyresultId);
  }
}
