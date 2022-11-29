import { Injectable } from '@angular/core';

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

export interface KeyResults {
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
export class KeyresultService {
  constructor() {}
}
