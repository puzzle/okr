import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Keyresult} from '../types/model/'

@Injectable({
  providedIn: 'root'
})
export class KeyresultService {
  constructor(private httpClient: HttpClient) { }

  getFullKeyResult(keyresultId: number) {
    return this.httpClient.get<Keyresult>('/api/v2/keyresults/' + keyresultId);
  }
}
