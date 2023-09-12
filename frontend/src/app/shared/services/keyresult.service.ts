import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {KeyResultMetric} from "../types/model/KeyResultMetric";

@Injectable({
  providedIn: 'root'
})
export class KeyresultService {
  constructor(private httpClient: HttpClient) { }

  getFullKeyResult(keyresultId: number) {
    return this.httpClient.get<KeyResultMetric>('/api/v2/keyresults/' + keyresultId);
  }
}
