import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { TeamService } from '../../services/team.service';
import { Observable, Subject, map, takeUntil } from 'rxjs';
import { Team } from '../../shared/types/model/team';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-team-list',
  templateUrl: './team-list.component.html',
  styleUrl: './team-list.component.scss',
  standalone: false
})
export class TeamListComponent implements OnInit, OnDestroy {
  private readonly route = inject(ActivatedRoute);

  public teams$: Observable<Team[]>;

  public selectedTeamId: number | undefined;

  private unsubscribe$ = new Subject<void>();

  constructor() {
    const teamService = inject(TeamService);
    this.teams$ = teamService.getAllTeams()
      .pipe(map((teams) => {
        return [...teams].sort((a, b) => {
          // --- RULE 1: Sort by Archived Status ---
          const aIsArchived = !!a.markedAsArchivedAt;
          const bIsArchived = !!b.markedAsArchivedAt;

          if (aIsArchived !== bIsArchived) {
            return aIsArchived ? 1 : -1;
          }

          // --- RULE 2: Sort Alphabetically ---
          return a.name.localeCompare(b.name);
        });
      }));
  }

  public ngOnInit(): void {
    this.route.paramMap.pipe(takeUntil(this.unsubscribe$))
      .subscribe((params) => {
        const teamId = params.get('teamId');
        this.selectedTeamId = teamId ? parseInt(teamId) : undefined;
      });
  }

  public ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }
}
