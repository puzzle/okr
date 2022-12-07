import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

export enum Unit {
  PERCENT = 'Prozent',
  CHF = 'CHF',
  NUMBER = 'Nummer',
  BINARY = 'Binär',
}

export enum ExpectedEvolution {
  INCREASE = 'Steigern',
  DECREASE = 'Verringern',
  CONSTANT = 'Konstant',
}

export interface Measure {
  id: number;
  keyResultId: number;
  value: number;
  changeInfo: string;
  initiatives: String;
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

  public getKeyResultById(keyResultId: number): Observable<KeyResultMeasure> {
    return this.httpClient.get<KeyResultMeasure>(
      '/api/v1/keyresults/' + keyResultId
    );
  }
}
