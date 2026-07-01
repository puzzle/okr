import { Component, input, Input } from '@angular/core';
import { UserTableEntry } from '../../../shared/types/model/user-table-entry';
import { MatTableDataSource } from '@angular/material/table';
import { Team } from '../../../shared/types/model/team';
import { getFullNameOfUser } from '../../../shared/types/model/user';
import { getRouteToUserDetails } from '../../../shared/route-utils';

@Component({
  selector: 'app-member-list-mobile',
  templateUrl: './member-list-mobile.component.html',
  styleUrl: './member-list-mobile.component.scss',
  standalone: false
})
export class MemberListMobileComponent {
  @Input({ required: true }) dataSource!: MatTableDataSource<UserTableEntry>;

  selectedTeam = input<Team>();

  getMemberDetailsLink(userTableEntry: UserTableEntry) {
    return getRouteToUserDetails(userTableEntry.id, this.selectedTeam()?.id);
  }

  protected readonly getFullNameFromUser = getFullNameOfUser;
}
