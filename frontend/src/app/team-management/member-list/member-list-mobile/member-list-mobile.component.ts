import { Component, Input, OnInit } from '@angular/core';
import { UserTableEntry } from '../../../shared/types/model/UserTableEntry';
import { MatTableDataSource } from '@angular/material/table';
import { BehaviorSubject } from 'rxjs';
import { Team } from '../../../shared/types/model/Team';
import { getFullNameOfUser } from '../../../shared/types/model/User';
import { getRouteToUserDetails } from '../../../shared/routeUtils';

@Component({
  selector: 'app-member-list-mobile',
  templateUrl: './member-list-mobile.component.html',
  styleUrl: './member-list-mobile.component.scss'
})
export class MemberListMobileComponent implements OnInit {
  @Input({ required: true }) dataSource!: MatTableDataSource<UserTableEntry>;

  @Input() selectedTeam$!: BehaviorSubject<Team | undefined>;

  constructor() {}

  ngOnInit(): void {}

  getMemberDetailsLink(userTableEntry: UserTableEntry) {
    return getRouteToUserDetails(userTableEntry.id, this.selectedTeam$.value?.id);
  }

  protected readonly getFullNameFromUser = getFullNameOfUser;
}
