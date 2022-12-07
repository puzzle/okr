import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface Objective {
  id: number;
  title: String;
  ownerId: number;
  ownerFirstname: String;
  ownerLastname: String;
  quarterId: number;
  quarterNumber: number;
  quarterYear: number;
  description: String;
  progress: number;
  created: string;
}

@Injectable({
  providedIn: 'root',
})
export class ObjectiveService {
  constructor(private httpClient: HttpClient) {}

  public getObjectivesOfTeam(teamId: number): Observable<Objective[]> {
    return this.httpClient
      .get<Objective[]>('api/v1/teams/' + teamId + '/objectives')
      .pipe(tap((data) => console.log(data)));
  }
}
