import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Team } from '../shared/types/model/Team';
import { BehaviorSubject, Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TeamService {
  constructor(private http: HttpClient) {}

  private teams: BehaviorSubject<Team[]> = new BehaviorSubject<Team[]>([]);
  private teamsLoaded = false;

  getAllTeams(): Observable<Team[]> {
    if (!this.teamsLoaded) {
      this.reloadTeams().subscribe();
      this.teamsLoaded = true;
    }
    return this.teams.asObservable();
  }

  reloadTeams(): Observable<Team[]> {
    return this.http.get<Team[]>('/api/v2/teams').pipe(
      tap((teams) => {
        if (!this.teams) {
          this.teams = new BehaviorSubject<Team[]>(teams);
          return;
        }
        this.teams.next(teams);
      }),
    );
  }

  createTeam(team: Team): Observable<Team> {
    return this.http.post<Team>('/api/v2/teams', team).pipe(tap(() => this.reloadTeams().subscribe()));
  }

  updateTeam(team: Team): Observable<Team> {
    return this.http.put<Team>(`/api/v2/teams/${team.id}`, team).pipe(tap(() => this.reloadTeams().subscribe()));
  }

  deleteTeam(id: number): Observable<any> {
    return this.http.delete(`/api/v2/teams/${id}`).pipe(tap(() => this.reloadTeams().subscribe()));
  }
}
