import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

export interface ExpectedEvolution {
  id: number;
  expectedEvolution: string;
}

export interface Unit {
  id: number;
  unit: string;
}

export interface Measure {
  id: number;
  keyResultId: number;
  value: number;
  changeInfo: string;
  initiatives: string;
  createdBy: number;
  createdOn: Date;
}

export interface KeyResultMeasure {
  id?: number | null;
  objectiveId: number;
  title: string;
  description: string;
  ownerId: number;
  ownerFirstname: string;
  ownerLastname: string;
  quarterId: number;
  quarterNumber: number;
  quarterYear: number;
  expectedEvolution: string;
  unit: string;
  basicValue: number;
  targetValue: number;
  measure?: Measure;
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

  getKeyResultById(keyresultId: number) {
    return this.httpClient.get<KeyResultMeasure>(
      '/api/v1/keyresults/' + keyresultId
    );
  }

  getInitKeyResult() {
    return {
      id: null,
      title: '',
      description: '',
      expectedEvolution: 'INCREASE',
      unit: 'PERCENT',
      ownerId: 0,
      ownerLastname: '',
      ownerFirstname: '',
      quarterId: 1,
      quarterNumber: 1,
      quarterYear: 2022,
      targetValue: 1,
      basicValue: 1,
      objectiveId: 1,
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
}
