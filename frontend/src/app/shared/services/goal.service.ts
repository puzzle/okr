import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from './user.service';

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
    owner: User;
  };
  teamId: number;
  teamName: string;
  progress: number;
  quarterLabel: string;
  expectedEvolution: string;
  unit: string;
  basicValue: number;
  targetValue: number;
  value: number;
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
