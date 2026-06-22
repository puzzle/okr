import { Component, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Team } from '../../shared/types/model/team';
import { ActivatedRoute } from '@angular/router';
import { ALL_TEAMS_STATE } from '../../services/team-state.tokens';

@Component({
  selector: 'app-team-list',
  templateUrl: './team-list.component.html',
  styleUrl: './team-list.component.scss',
  standalone: false
})
export class TeamListComponent {
  private readonly route = inject(ActivatedRoute);

  private readonly teamStateService = inject(ALL_TEAMS_STATE);

  public teams$: Observable<Team[]>;

  public selectedTeamId: number | undefined;


  constructor() {
    this.teams$ = this.teamStateService.getTeams()
      .pipe(map((teams) => {
        return [...teams].sort((a, b) => {
          const aIsArchived = !!a.markedAsArchivedAt;
          const bIsArchived = !!b.markedAsArchivedAt;
          if (aIsArchived !== bIsArchived) {
            return aIsArchived ? 1 : -1;
          }
          return a.name.localeCompare(b.name);
        });
      }));

    this.route.paramMap
      .pipe(takeUntilDestroyed())
      .subscribe((params) => {
        const teamId = params.get('teamId');
        this.selectedTeamId = teamId ? parseInt(teamId) : undefined;
      });
  }
}
