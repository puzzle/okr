import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Objective } from '../models/Objective';

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
