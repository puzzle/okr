import { Component, inject, computed } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
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

  public teams = computed(() => {
    const allTeams = this.teamStateService.getTeams()();

    return [...allTeams].sort((a, b) => {
      const aIsArchived = !!a.markedAsArchivedAt;
      const bIsArchived = !!b.markedAsArchivedAt;

      if (aIsArchived !== bIsArchived) {
        return aIsArchived ? 1 : -1;
      }
      return a.name.localeCompare(b.name);
    });
  });

  public selectedTeamId: number | undefined;

  constructor() {
    this.route.paramMap
      .pipe(takeUntilDestroyed())
      .subscribe((params) => {
        const teamId = params.get('teamId');
        this.selectedTeamId = teamId ? parseInt(teamId) : undefined;
      });
  }
}
