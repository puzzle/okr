import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Objective {
  id: number | null;
  title: string;
  ownerId: number | null;
  ownerFirstname: string;
  ownerLastname: string;
  teamId: number | null;
  teamName: string;
  quarterId: number;
  quarterNumber: number;
  quarterYear: number;
  description: string;
  progress: number;
  created: string;
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

  public saveObjective(
    objective: Objective,
    post: boolean
  ): Observable<Objective> {
    if (post) {
      return this.httpClient.post<Objective>('api/v1/objectives', objective);
    } else {
      return this.httpClient.put<Objective>(
        'api/v1/objectives/' + objective.id,
        objective
      );
    }
  }

  public getInitObjective() {
    return {
      id: null,
      title: '',
      description: '',
      teamId: null,
      quarterId: 1,
      ownerId: null,
      ownerFirstname: '',
      ownerLastname: '',
      quarterYear: 2022,
      quarterNumber: 1,
      teamName: '',
      progress: 0,
      created: '',
    };
  }
}
