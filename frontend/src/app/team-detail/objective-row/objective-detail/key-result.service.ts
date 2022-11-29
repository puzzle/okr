import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
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

export interface KeyResult {
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
}

@Injectable({
  providedIn: 'root',
})
export class KeyResultService {
  constructor(private _httpClient: HttpClient) {}

  public getKeyResultsOfObjective(
    objectiveId: number
  ): Observable<KeyResult[]> {
    return this._httpClient
      .get<KeyResult[]>('/api/v1/objectives/' + objectiveId + '/keyresults')
      .pipe(tap((data) => console.log(data)));
  }
}
