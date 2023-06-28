import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { map, Observable, tap } from 'rxjs';
import * as http from 'http';

export interface Objective {
  id: number | null;
  title: string;
  ownerId: number | null;
  ownerFirstname: string;
  ownerLastname: string;
  teamId: number | null;
  teamName: string;
  quarterId: number | null;
  quarterLabel: string | null;
  description: string;
  progress: number | null;
  created: string;
}

@Injectable({
  providedIn: 'root',
})
export class ObjectiveService {
  constructor(private httpClient: HttpClient) {}

  public getObjectivesOfTeam(teamId: number): Observable<Objective[]> {
    return this.httpClient.get<Objective[]>('api/v1/teams/' + teamId + '/objectives');
  }

  public getObjectiveById(objectiveId: number): Observable<Objective> {
    return this.httpClient.get<Objective>('api/v1/objectives/' + objectiveId);
  }

  public saveObjective(objective: Objective, post: boolean): Observable<any> {
    objective.progress = null;
    if (post) {
      return this.httpClient.post<Objective>('api/v1/objectives', objective);
    } else {
      objective.quarterLabel = null;
      return this.httpClient
        .put<Objective>('api/v1/objectives/' + objective.id, objective, { observe: 'response' })
        .pipe(
          map((response) => ({
            status: response.status,
            body: response.body,
          }))
        );
    }
  }

  public getInitObjective(): Objective {
    return {
      id: null,
      title: '',
      description: '',
      teamId: null,
      ownerId: null,
      ownerFirstname: '',
      ownerLastname: '',
      quarterId: null,
      quarterLabel: '',
      teamName: '',
      progress: 0,
      created: '',
    };
  }

  deleteObjectiveById(objectiveId: number): Observable<Objective> {
    return this.httpClient.delete<Objective>('/api/v1/objectives/' + objectiveId);
  }
}
