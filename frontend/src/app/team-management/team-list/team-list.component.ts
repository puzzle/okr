import { Component, OnInit, inject } from '@angular/core';
import { TeamService } from '../../services/team.service';
import { Observable, map } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Team } from '../../shared/types/model/team';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-team-list',
  templateUrl: './team-list.component.html',
  styleUrl: './team-list.component.scss',
  standalone: false
})
export class TeamListComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);

  private readonly teamService = inject(TeamService);

  public teams$: Observable<Team[]>;

  public selectedTeamId: number | undefined;


  constructor() {
    this.teams$ = this.teamService.getTeams()
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
    this.teamService.loadTeams();
  }
}
