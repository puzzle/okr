import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import { of } from 'rxjs';
import { MemberListComponent } from './member-list.component';
import { UserService } from '../../services/user.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { User } from '../../shared/types/model/User';
import { team1, team2, team3, testUser, users } from '../../shared/testData';
import { convertFromUser, convertFromUsers, UserTableEntry } from '../../shared/types/model/UserTableEntry';
import { UserRole } from '../../shared/types/enums/UserRole';
import { TeamService } from '../../services/team.service';
import { MatDialog } from '@angular/material/dialog';
import { AddMemberToTeamDialogComponent } from '../add-member-to-team-dialog/add-member-to-team-dialog.component';
import { OKR_DIALOG_CONFIG } from '../../shared/constantLibary';
import { AddEditTeamDialog } from '../add-edit-team-dialog/add-edit-team-dialog.component';
import { TranslateTestingModule } from 'ngx-translate-testing';

const userServiceMock = {
  getUsers: jest.fn(),
  reloadUsers: jest.fn(),
  reloadCurrentUser: jest.fn(),
};

const teamServiceMock = {
  getAllTeams: jest.fn(),
  deleteTeam: jest.fn(),
  removeUserFromTeam: jest.fn(),
};

const activatedRouteMock = {
  paramMap: {} as any,
};

const routerMock = {
  navigateByUrl: jest.fn(),
};

const dialogMock = {
  open: jest.fn(),
};

describe('MemberListComponent', () => {
  let component: MemberListComponent;
  let fixture: ComponentFixture<MemberListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MemberListComponent],
      imports: [HttpClientTestingModule, TranslateTestingModule],
      providers: [
        { provide: UserService, useValue: userServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: TeamService, useValue: teamServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: MatDialog, useValue: dialogMock },
        ChangeDetectorRef,
      ],
    });

    fixture = TestBed.createComponent(MemberListComponent);
    component = fixture.componentInstance;

    activatedRouteMock.paramMap = of({
      get: () => team1.id,
    });

    userServiceMock.reloadCurrentUser.mockReset();
    userServiceMock.reloadUsers.mockReset();

    jest.spyOn(userServiceMock, 'getUsers').mockReturnValue(of([]));
    userServiceMock.reloadCurrentUser.mockReturnValue(of(testUser));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should test method convertFromUser', () => {
    const user: User = { ...testUser };
    let userTableEntry = convertFromUser(user);

    expect(userTableEntry.id).toBe(user.id);
    expect(userTableEntry.firstname).toBe(user.firstname);
    expect(userTableEntry.lastname).toBe(user.lastname);
    expect(userTableEntry.email).toBe(user.email);
    expect(userTableEntry.roles).toStrictEqual([UserRole.TEAM_MEMBER]);
    expect(userTableEntry.teams).toStrictEqual([team1.name]);

    user.userTeamList.push({
      id: 2,
      team: team2,
      isTeamAdmin: true,
    });
    user.isOkrChampion = true;

    userTableEntry = convertFromUser(user);

    expect(userTableEntry.roles).toStrictEqual([UserRole.OKR_CHAMPION, UserRole.TEAM_ADMIN, UserRole.TEAM_MEMBER]);
    expect(userTableEntry.teams).toStrictEqual([team1.name, team2.name]);
  });

  it('should test method convertFromUsers', () => {
    const usersCopy: User[] = JSON.parse(JSON.stringify(users));
    usersCopy[0].userTeamList.push({
      id: 1,
      team: team1,
      isTeamAdmin: true,
    });
    usersCopy[0].isOkrChampion = true;
    usersCopy[1].userTeamList.push({
      id: 2,
      team: team1,
      isTeamAdmin: false,
    });
    usersCopy[2].userTeamList.push({
      id: 3,
      team: team2,
      isTeamAdmin: false,
    });
    usersCopy[3].userTeamList.push(
      {
        id: 4,
        team: team1,
        isTeamAdmin: false,
      },
      {
        id: 5,
        team: team2,
        isTeamAdmin: true,
      },
    );

    let userTableEntries = convertFromUsers(usersCopy, null);
    expect(userTableEntries.length).toBe(4);
    // test that it makes a deep copy
    expect(userTableEntries).not.toBe(usersCopy);
    // should be sorted
    expect(userTableEntries.map((ut) => ut.firstname)).toStrictEqual(['Bob', 'Key Result', 'Paco', 'Robin']);
  });

  it('ngOnInit should load all Users, set teamId, selectedTeam and update data source correctly', fakeAsync(() => {
    TestBed.runInInjectionContext(() => {
      userServiceMock.getUsers.mockReturnValue(of(users));
      teamServiceMock.getAllTeams.mockReturnValue(of([team1, team2, team3]));
      component.ngOnInit();
      tick();
      expect(teamServiceMock.getAllTeams).toHaveBeenCalledTimes(1);
      expect(component.selectedTeam).toBe(team1);
      expect(component.dataSource.length).toBe(1);
      expect(component.dataSource[0].teams[0]).toBe(team1.name);
    });
  }));

  it('ngOnInit should load all Users, set teamId, selectedTeam and update data source correctly if teamIdParam is null', fakeAsync(() => {
    activatedRouteMock.paramMap = of({
      get: () => null,
    });
    TestBed.runInInjectionContext(() => {
      userServiceMock.getUsers.mockReturnValue(of(users));
      teamServiceMock.getAllTeams.mockReturnValue(of([team1, team2, team3]));
      component.ngOnInit();
      tick();
      expect(component.selectedTeam).toBe(undefined);
      expect(component.dataSource.length).toBe(users.length);
    });
  }));

  it('should set displayedColumns correctly', fakeAsync(() => {
    const teamCopy = { ...team1 };
    teamCopy.writeable = true;
    TestBed.runInInjectionContext(() => {
      userServiceMock.getUsers.mockReturnValue(of(users));
      teamServiceMock.getAllTeams.mockReturnValue(of([teamCopy]));
      component.ngOnInit();
      tick();
      expect(component.displayedColumns).toStrictEqual(['icon', 'name', 'roles', 'delete']);
    });
  }));

  it('deleteTeam should trigger teamService.deleteTeam and navigate', fakeAsync(() => {
    routerMock.navigateByUrl.mockReturnValue(of(null));
    teamServiceMock.deleteTeam.mockReturnValue(of(null));

    const team = team1;

    component.deleteTeam(team);
    tick();

    expect(teamServiceMock.deleteTeam).toBeCalledTimes(1);
    expect(teamServiceMock.deleteTeam).toBeCalledWith(team.id);
    expect(routerMock.navigateByUrl).toBeCalledWith('team-management');
    expect(routerMock.navigateByUrl).toBeCalledTimes(1);
    expect(userServiceMock.reloadUsers).toBeCalledTimes(1);
    expect(userServiceMock.reloadCurrentUser).toBeCalledTimes(1);
  }));

  it('addMemberToTeam should open dialog', () => {
    component.selectedTeam = team1;
    component.dataSource = [];
    dialogMock.open.mockReturnValue({
      afterClosed: () => of(null),
    });
    component.addMemberToTeam();

    const expectedDialogConfig = OKR_DIALOG_CONFIG;
    expectedDialogConfig.data = {
      team: team1,
      currentUsersOfTeam: component.dataSource,
    };

    expect(dialogMock.open).toBeCalledWith(AddMemberToTeamDialogComponent, expectedDialogConfig);
  });

  it('should showInvitePerson only if selected team is null', () => {
    component.selectedTeam = undefined;
    expect(component.showInvitePerson()).toBeTruthy();
    component.selectedTeam = team1;
    expect(component.showInvitePerson()).toBeFalsy();
  });

  it('should showAddMemberToTeam if selectedTeam is set and selectedTeam is writable', () => {
    component.selectedTeam = undefined;
    expect(component.showAddMemberToTeam()).toBeFalsy();
    const teamCopy = { ...team1 };
    teamCopy.writeable = false;
    component.selectedTeam = teamCopy;
    expect(component.showAddMemberToTeam()).toBeFalsy();
    teamCopy.writeable = true;
    expect(component.showAddMemberToTeam()).toBeTruthy();
  });

  it('edit team should open dialog', () => {
    component.selectedTeam = team1;
    dialogMock.open.mockReturnValue({
      afterClosed: () => of(null),
    });
    component.editTeam();

    const expectedDialogConfig = OKR_DIALOG_CONFIG;
    expectedDialogConfig.data = {
      team: team1,
    };

    expect(dialogMock.open).toBeCalledWith(AddEditTeamDialog, expectedDialogConfig);
  });

  it('should return correct memberDetailsLink', () => {
    expect(component.getMemberDetailsLink(testUser)).toStrictEqual('/team-management/details/member/' + testUser.id);
  });

  it('removeMemberFromTeam should call removeUserFromTeam and reloadUsers', fakeAsync(() => {
    const entry = {
      id: 1,
    };
    component.selectedTeam = team1;
    teamServiceMock.removeUserFromTeam.mockReturnValue(of(null));
    userServiceMock.reloadUsers.mockReturnValue(of());

    component.removeMemberFromTeam(entry as UserTableEntry, new MouseEvent('click'));
    tick();

    expect(teamServiceMock.removeUserFromTeam).toBeCalledTimes(1);
    expect(teamServiceMock.removeUserFromTeam).toBeCalledWith(entry.id, component.selectedTeam);
    expect(userServiceMock.reloadUsers).toBeCalledTimes(1);
    expect(userServiceMock.reloadCurrentUser).toBeCalledTimes(1);
  }));
});
