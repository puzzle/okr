import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Organisation } from '../types/model/Organisation';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class OrganisationService {
  constructor(private http: HttpClient) {}

  getOrganisations(): Observable<Organisation[]> {
    return this.http.get<Organisation[]>('/api/v1/organisations');
  }

  getOrganisationsByTeamId(teamId: number): Observable<Organisation[]> {
    return this.http.get<Organisation[]>('/api/v1/organisations/' + teamId);
  }
}
