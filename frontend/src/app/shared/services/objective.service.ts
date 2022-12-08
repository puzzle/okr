import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Objective {
  id: number | null;
  title: string;
  ownerId: number;
  ownerFirstname: string;
  ownerLastname: string;
  teamId: number | null;
  quarterId: number;
  quarterNumber: number;
  quarterYear: number;
  description: string;
  progress: number;
}

@Injectable({
  providedIn: 'root',
})
export class ObjectiveService {
  constructor(private httpClient: HttpClient) {}

  public getObjectivesOfTeam(teamId: number): Observable<Objective[]> {
    return this.httpClient.get<Objective[]>(
      'api/v1/teams/' + teamId + '/objectives'
    );
  }

  public getObjectiveById(objectiveId: number): Observable<Objective> {
    return this.httpClient.get<Objective>('api/v1/objectives/' + objectiveId);
  }

  public saveObjective(objective: Objective): Observable<Objective> {
    return this.httpClient.post<Objective>('api/v1/objectives', objective);
  }

  public getInitObjective() {
    return {
      id: null,
      title: '',
      description: '',
      teamId: null,
      quarterId: 1,
      ownerId: 0,
      ownerFirstname: '',
      ownerLastname: '',
      quarterYear: 2022,
      quarterNumber: 1,
      teamName: '',
      progress: 0,
    };
  }
}
