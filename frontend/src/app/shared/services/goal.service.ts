import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Goal {
  objective: {
    id: number;
    title: string;
    description: string;
  };
  keyresult: {
    id: number;
    title: string;
    description: string;
  };
  teamId: number;
  teamName: string;
  progress: number;
  quarterNumber: number;
  quarterYear: number;
  expectedEvolution: string;
  unit: string;
  basicValue: number;
  targetValue: number;
}

@Injectable({
  providedIn: 'root',
})
export class GoalService {
  constructor(private httpClient: HttpClient) {}

  public getGoalByKeyResultId(keyResultId: number): Observable<Goal> {
    return this.httpClient.get<Goal>('/api/v1/goals/' + keyResultId);
  }
}
