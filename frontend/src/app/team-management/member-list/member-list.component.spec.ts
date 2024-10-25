import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { ActivatedRoute, provideRouter, Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import { of } from 'rxjs';
import { MemberListComponent } from './member-list.component';
import { UserService } from '../../services/user.service';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { User } from '../../shared/types/model/User';
import { team1, team2, team3, testUser, users } from '../../shared/testData';
import { convertFromUser, convertFromUsers, UserTableEntry } from '../../shared/types/model/UserTableEntry';
import { UserRole } from '../../shared/types/enums/UserRole';
import { TeamService } from '../../services/team.service';
import { AddMemberToTeamDialogComponent } from '../add-member-to-team-dialog/add-member-to-team-dialog.component';
import { AddEditTeamDialog } from '../add-edit-team-dialog/add-edit-team-dialog.component';
import { TranslateTestingModule } from 'ngx-translate-testing';
import { MatTableDataSource } from '@angular/material/table';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MemberListTableComponent } from './member-list-table/member-list-table.component';
import { MemberListMobileComponent } from './member-list-mobile/member-list-mobile.component';
import { DialogService } from '../../services/dialog.service';

const userServiceMock = {
  getUsers: jest.fn(),
  reloadUsers: jest.fn(),
  reloadCurrentUser: jest.fn(),
  getCurrentUser: jest.fn(),
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

const dialogService = {
  open: jest.fn(),
  openConfirmDialog: jest.fn(),
};

describe('MemberListComponent', () => {
  let component: MemberListComponent;
  let fixture: ComponentFixture<MemberListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MemberListComponent, MemberListTableComponent, MemberListMobileComponent],
      imports: [TranslateTestingModule, BrowserAnimationsModule],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: UserService, useValue: userServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: TeamService, useValue: teamServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: DialogService, useValue: dialogService },
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
    userServiceMock.getCurrentUser.mockReturnValue(of(testUser));
  });

  afterEach(() => {
    teamServiceMock.deleteTeam.mockReset();
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
    expect(userTableEntry.isOkrChampion).toBeFalsy();
    expect(userTableEntry.teams).toStrictEqual([team1.name]);
    expect(userTableEntry.userTeamList).toStrictEqual(user.userTeamList);

    user.userTeamList.push({
      id: 2,
      team: team2,
      isTeamAdmin: true,
    });
    user.isOkrChampion = true;

    userTableEntry = convertFromUser(user);

    expect(userTableEntry.roles).toStrictEqual([UserRole.TEAM_ADMIN, UserRole.TEAM_MEMBER]);
    expect(userTableEntry.isOkrChampion).toBeTruthy();
    expect(userTableEntry.teams).toStrictEqual([team1.name, team2.name]);
    expect(userTableEntry.userTeamList).toStrictEqual(user.userTeamList);
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

  it('ngAfterViewInit should load all Users, set teamId, selectedTeam and update data source correctly', fakeAsync(() => {
    userServiceMock.getUsers.mockReturnValue(of(users));
    teamServiceMock.getAllTeams.mockReturnValue(of([team1, team2, team3]));
    component.ngAfterViewInit();
    tick();
    expect(teamServiceMock.getAllTeams).toHaveBeenCalledTimes(1);
    expect(component.selectedTeam$.value).toBe(team1);
    expect(component.dataSource.data.length).toBe(1);
    expect(component.dataSource.data[0].teams[0]).toBe(team1.name);
  }));

  it('ngAfterViewInit should load all Users, set teamId, selectedTeam and update data source correctly if teamIdParam is null', fakeAsync(() => {
    activatedRouteMock.paramMap = of({
      get: () => null,
    });
    TestBed.runInInjectionContext(() => {
      userServiceMock.getUsers.mockReturnValue(of(users));
      teamServiceMock.getAllTeams.mockReturnValue(of([team1, team2, team3]));
      component.ngAfterViewInit();
      tick();
      expect(component.selectedTeam$.value).toBe(undefined);
      expect(component.dataSource.data.length).toBe(users.length);
    });
  }));

  it('deleteTeam should trigger teamService.deleteTeam and navigate', fakeAsync(() => {
    routerMock.navigateByUrl.mockReturnValue(of(null));
    teamServiceMock.deleteTeam.mockReturnValue(of(null));
    dialogService.open.mockReturnValue({
      afterClosed: () => of(true),
    });

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

  it('deleteTeam should not trigger teamService.deleteTeam if dialog is canceled', fakeAsync(() => {
    routerMock.navigateByUrl.mockReturnValue(of(null));
    teamServiceMock.deleteTeam.mockReturnValue(of(null));
    dialogService.open.mockReturnValue({
      afterClosed: () => of(false),
    });

    component.deleteTeam(team1);
    tick();

    expect(teamServiceMock.deleteTeam).toBeCalledTimes(0);
  }));

  it('addMemberToTeam should open dialog', () => {
    component.selectedTeam$.next(team1);
    component.dataSource = new MatTableDataSource<UserTableEntry>([]);
    dialogService.open.mockReturnValue({
      afterClosed: () => of(null),
    });
    component.addMemberToTeam();

    expect(dialogService.open).toBeCalledWith(
      AddMemberToTeamDialogComponent,
      expect.objectContaining({
        data: {
          team: team1,
          currentUsersOfTeam: component.dataSource.filteredData,
        },
      }),
    );
  });

  it('should showAddMemberToTeam if selectedTeam is set and selectedTeam is writable', () => {
    component.selectedTeam$.next(undefined);
    expect(component.showAddMemberToTeam()).toBeFalsy();
    const teamCopy = { ...team1 };
    teamCopy.writeable = false;
    component.selectedTeam$.next(teamCopy);
    expect(component.showAddMemberToTeam()).toBeFalsy();
    teamCopy.writeable = true;
    expect(component.showAddMemberToTeam()).toBeTruthy();
  });

  it('edit team should open dialog', () => {
    component.selectedTeam$.next(team1);
    dialogService.open.mockReturnValue({
      afterClosed: () => of(null),
    });
    component.editTeam();

    expect(dialogService.open).toBeCalledWith(
      AddEditTeamDialog,
      expect.objectContaining({
        data: {
          team: team1,
        },
      }),
    );
  });
});
