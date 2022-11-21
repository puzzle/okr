import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Team } from '../dashboard/team.service';

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
}

@Injectable({
  providedIn: 'root',
})
export class ObjectiveService {
  constructor(private _httpClient: HttpClient) {}

  public getObjectivesOfTeam(teamId: number): Observable<Objective[]> {
    return this._httpClient
      .get<Objective[]>(
        'http://localhost:4200/api/v1/teams/' + teamId + '/objectives'
      )
      .pipe(tap((data) => console.log(data)));
  }
}
