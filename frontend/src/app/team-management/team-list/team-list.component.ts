import { Component, OnInit, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Team } from '../../shared/types/model/team';
import { ActivatedRoute } from '@angular/router';
import { TeamStateService } from '../../services/team.state.service';

@Component({
  selector: 'app-team-list',
  templateUrl: './team-list.component.html',
  styleUrl: './team-list.component.scss',
  standalone: false
})
export class TeamListComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);

  private readonly teamStateService = inject(TeamStateService);

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

  public ngOnInit(): void {
    this.teamStateService.loadTeams();
  }
}
