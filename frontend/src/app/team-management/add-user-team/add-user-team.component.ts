import { Component, EventEmitter, Output, inject, computed, input } from '@angular/core';
import { Team } from '../../shared/types/model/team';
import { UserTeam } from '../../shared/types/model/user-team';
import { ALL_TEAMS_STATE } from '../../services/team-state.tokens';

@Component({
  selector: 'app-add-user-team',
  templateUrl: './add-user-team.component.html',
  styleUrl: './add-user-team.component.scss',
  standalone: false
})
export class AddUserTeamComponent {
  private readonly teamStateService = inject(ALL_TEAMS_STATE);

  @Output()
  addUserTeam = new EventEmitter<UserTeam>();

  public currentTeams = input.required<UserTeam[]>();

  userTeam: { team: Team;
    isTeamAdmin: boolean; } | undefined;

  public allAdminTeams = computed(() => {
    const teams = this.teamStateService.getTeams()();
    return teams.filter((t) => t.isWriteable);
  });

  public selectableAdminTeams = computed(() => {
    const allTeams = this.allAdminTeams();

    const userTeams = this.currentTeams();

    const currentTeamIds = userTeams.map((ut) => ut.team.id);
    return allTeams.filter((t) => !currentTeamIds.includes(t.id));
  });

  createUserTeam(team: Team) {
    this.userTeam = {
      team,
      isTeamAdmin: false
    };
  }

  save(): void {
    if (!this.userTeam) {
      throw new Error('UserTeam should be defined here');
    }
    this.addUserTeam.emit(this.userTeam);
    this.userTeam = undefined;
  }

  isAddButtonVisible(adminTeams: Team[] | null) {
    return !this.userTeam && adminTeams?.length;
  }

  isAddButtonDisabled(selectableAdminTeams: Team[] | null) {
    return !selectableAdminTeams?.length;
  }
}
