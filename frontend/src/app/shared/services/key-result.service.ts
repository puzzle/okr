import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

export interface Measure {
  id: number;
  keyResultId: number;
  value: number;
  changeInfo: string;
  initiatives: string;
  createdById: number;
  createdOn: string;
  measureDate: string;
}

export interface KeyResultMeasure {
  id: number | null;
  objectiveId: number;
  title: string;
  description: string;
  ownerId: number;
  ownerFirstname: string;
  ownerLastname: string;
  expectedEvolution: string;
  unit: string;
  basicValue: number;
  targetValue: number;
  measure?: Measure;
  progress: number;
}

@Injectable({
  providedIn: 'root',
})
export class KeyResultService {
  constructor(private httpClient: HttpClient) {}

  public getKeyResultsOfObjective(
    objectiveId: number
  ): Observable<KeyResultMeasure[]> {
    return this.httpClient.get<KeyResultMeasure[]>(
      '/api/v1/objectives/' + objectiveId + '/keyresults'
    );
  }

  public getMeasuresOfKeyResult(keyresultId: number): Observable<Measure[]> {
    return this.httpClient.get<Measure[]>(
      '/api/v1/keyresults/' + keyresultId + '/measures'
    );
  }

  getKeyResultById(keyresultId: number) {
    return this.httpClient.get<KeyResultMeasure>(
      '/api/v1/keyresults/' + keyresultId
    );
  }

  getInitKeyResult(): KeyResultMeasure {
    return {
      id: null,
      title: '',
      description: '',
      expectedEvolution: '',
      unit: '',
      ownerId: 0,
      ownerLastname: '',
      ownerFirstname: '',
      targetValue: 1,
      basicValue: 1,
      objectiveId: 1,
      progress: 0,
    };
  }

  saveKeyresult(keyresult: KeyResultMeasure, post: boolean) {
    if (post) {
      return this.httpClient.post<KeyResultMeasure>(
        `/api/v1/keyresults`,
        keyresult
      );
    } else {
      return this.httpClient.put<KeyResultMeasure>(
        `/api/v1/keyresults/` + keyresult.id,
        keyresult
      );
    }
  }

  deleteKeyResultById(keyresultId: number): Observable<KeyResultMeasure> {
    return this.httpClient.delete<KeyResultMeasure>(
      '/api/v1/keyresults/' + keyresultId
    );
  }
}
