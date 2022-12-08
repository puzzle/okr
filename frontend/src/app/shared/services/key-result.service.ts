import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

export enum ExpectedEvolution {
  INCREASE,
  DECREASE,
  CONSTANT,
}

export enum Unit {
  PERCENT,
  CHF,
  NUMBER,
  BINARY,
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
  id: number;
  objectiveId: number;
  title: string;
  description: string;
  ownerId: number;
  ownerFirstname: string;
  ownerLastname: string;
  quarterId: number;
  quarterNumber: number;
  quarterYear: number;
  expectedEvolution: ExpectedEvolution;
  unit: Unit;
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
      id: 0,
      title: '',
      description: '',
      expectedEvolution: ExpectedEvolution.CONSTANT,
      unit: Unit.BINARY,
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

  saveKeyresult(keyresult: KeyResultMeasure) {
    return this.httpClient.post<KeyResultMeasure>(`/users`, keyresult);
  }
}
