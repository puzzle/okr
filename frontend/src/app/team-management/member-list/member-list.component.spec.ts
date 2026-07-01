import { ActivatedRoute, provideRouter, Router } from '@angular/router';
import { ChangeDetectorRef, signal } from '@angular/core';
import { BehaviorSubject, of } from 'rxjs';
import { MemberListComponent } from './member-list.component';
import { UserService } from '../../services/user.service';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { User } from '../../shared/types/model/user';
import { team1, team2, team3, testUser, users } from '../../shared/test-data';
import { convertFromUser, convertFromUsers } from '../../shared/types/model/user-table-entry';
import { UserRole } from '../../shared/types/enums/user-role';
import { AddMemberToTeamDialogComponent } from '../add-member-to-team-dialog/add-member-to-team-dialog.component';
import { AddEditTeamDialogComponent } from '../add-edit-team-dialog/add-edit-team-dialog.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MemberListTableComponent } from './member-list-table/member-list-table.component';
import { MemberListMobileComponent } from './member-list-mobile/member-list-mobile.component';
import { DialogService } from '../../services/dialog.service';
import { provideHttpClient } from '@angular/common/http';
import { ArchiveTeamDialogComponent } from '../../shared/dialog/archive-dialog/archive-dialog.component';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { ALL_TEAMS_STATE } from '../../services/team-state.tokens';
import { Team } from '../../shared/types/model/team';
import { MatMenuModule } from '@angular/material/menu';

const userServiceMock = {
  getUsers: jest.fn(),
  reloadUsers: jest.fn(),
  reloadCurrentUser: jest.fn(),
  getCurrentUser: jest.fn()
};

const teamStateServiceMock = {
  loadTeams: jest.fn(),
  getTeams: jest.fn(),
  deleteTeam: jest.fn(),
  archiveTeam: jest.fn(),
  unarchiveTeam: jest.fn()
};

const paramMapSubject = new BehaviorSubject<any>({ get: () => team1.id.toString() });

const activatedRouteMock = {
  paramMap: paramMapSubject.asObservable()
};

const routerMock = {
  navigateByUrl: jest.fn()
};

const dialogService = {
  open: jest.fn(),
  openConfirmDialog: jest.fn()
};

describe('MemberListComponent', () => {
  let component: MemberListComponent;
  let fixture: ComponentFixture<MemberListComponent>;

  const mockTeamsSignal = signal<Team[]>([]);

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MemberListComponent,
        MemberListTableComponent,
        MemberListMobileComponent],
      imports: [BrowserAnimationsModule,
        MatMenuModule],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: UserService,
          useValue: userServiceMock },
        { provide: ActivatedRoute,
          useValue: activatedRouteMock },
        { provide: ALL_TEAMS_STATE,
          useValue: teamStateServiceMock },
        { provide: Router,
          useValue: routerMock },
        { provide: DialogService,
          useValue: dialogService },
        ChangeDetectorRef
      ]
    });

    userServiceMock.getUsers.mockReturnValue(of(users));
    teamStateServiceMock.getTeams.mockReturnValue(signal([team1,
      team2,
      team3]));
    userServiceMock.reloadCurrentUser.mockReturnValue(of(testUser));
    userServiceMock.getCurrentUser.mockReturnValue(testUser);
    paramMapSubject.next({ get: () => team1.id.toString() });

    mockTeamsSignal.set([team1,
      team2,
      team3]);
    teamStateServiceMock.getTeams.mockReturnValue(mockTeamsSignal);

    fixture = TestBed.createComponent(MemberListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should test method convertFromUser', () => {
    const user: User = { ...testUser };
    let userTableEntry = convertFromUser(user);

    expect(userTableEntry.id)
      .toBe(user.id);
    expect(userTableEntry.firstName)
      .toBe(user.firstName);
    expect(userTableEntry.lastName)
      .toBe(user.lastName);
    expect(userTableEntry.email)
      .toBe(user.email);
    expect(userTableEntry.roles)
      .toStrictEqual([UserRole.TEAM_MEMBER]);
    expect(userTableEntry.isOkrChampion)
      .toBeFalsy();
    expect(userTableEntry.teams)
      .toStrictEqual([team1.name]);
    expect(userTableEntry.userTeamList)
      .toStrictEqual(user.userTeamList);

    user.userTeamList.push({ id: 2,
      team: team2,
      isTeamAdmin: true });
    user.isOkrChampion = true;

    userTableEntry = convertFromUser(user);

    expect(userTableEntry.roles)
      .toStrictEqual([UserRole.TEAM_ADMIN,
        UserRole.TEAM_MEMBER]);
    expect(userTableEntry.isOkrChampion)
      .toBeTruthy();
    expect(userTableEntry.teams)
      .toStrictEqual([team1.name,
        team2.name]);
    expect(userTableEntry.userTeamList)
      .toStrictEqual(user.userTeamList);
  });

  it('should test method convertFromUsers', () => {
    const usersCopy: User[] = JSON.parse(JSON.stringify(users));
    usersCopy[0].userTeamList.push({ id: 1,
      team: team1,
      isTeamAdmin: true });
    usersCopy[0].isOkrChampion = true;
    usersCopy[1].userTeamList.push({ id: 2,
      team: team1,
      isTeamAdmin: false });
    usersCopy[2].userTeamList.push({ id: 3,
      team: team2,
      isTeamAdmin: false });
    usersCopy[3].userTeamList.push({ id: 4,
      team: team1,
      isTeamAdmin: false }, { id: 5,
      team: team2,
      isTeamAdmin: true });

    const userTableEntries = convertFromUsers(usersCopy, null);
    expect(userTableEntries.length)
      .toBe(4);
    expect(userTableEntries).not.toBe(usersCopy);
    expect(userTableEntries.map((ut) => ut.firstName))
      .toStrictEqual([
        'Bob',
        'Key Result',
        'Paco',
        'Robin'
      ]);
  });

  it('should load all Users, set teamId, selectedTeam and update data source correctly', () => {
    paramMapSubject.next({ get: () => team1.id.toString() });
    fixture.detectChanges();

    expect(component.selectedTeam()?.id)
      .toBe(team1.id);
    expect(component.dataSource.data.length)
      .toBe(1);
    expect(component.dataSource.data[0].teams[0])
      .toBe(team1.name);
  });

  it('should load all Users, set selectedTeam to undefined and update data source correctly if teamIdParam is null', () => {
    paramMapSubject.next({ get: () => null });
    fixture.detectChanges();

    expect(component.selectedTeam())
      .toBe(undefined);
    expect(component.dataSource.data.length)
      .toBe(users.length);
  });

  it('deleteTeam should trigger teamStateService.deleteTeam and navigate', fakeAsync(() => {
    routerMock.navigateByUrl.mockReturnValue(of(null));
    teamStateServiceMock.deleteTeam.mockReturnValue(of(null));
    dialogService.openConfirmDialog.mockReturnValue({ afterClosed: () => of(true) });

    const team = team1;
    component.deleteTeam(team);
    tick();

    expect(teamStateServiceMock.deleteTeam)
      .toHaveBeenCalledTimes(1);
    expect(teamStateServiceMock.deleteTeam)
      .toHaveBeenCalledWith(team.id);
    expect(routerMock.navigateByUrl)
      .toHaveBeenCalledWith('team-management');
    expect(routerMock.navigateByUrl)
      .toHaveBeenCalledTimes(1);
    expect(userServiceMock.reloadUsers)
      .toHaveBeenCalledTimes(1);
    expect(userServiceMock.reloadCurrentUser)
      .toHaveBeenCalledTimes(1);
  }));

  it('deleteTeam should not trigger teamStateService.deleteTeam if dialog is canceled', fakeAsync(() => {
    routerMock.navigateByUrl.mockReturnValue(of(null));
    teamStateServiceMock.deleteTeam.mockReturnValue(of(null));
    dialogService.openConfirmDialog.mockReturnValue({ afterClosed: () => of(false) });

    component.deleteTeam(team1);
    tick();

    expect(teamStateServiceMock.deleteTeam)
      .toHaveBeenCalledTimes(0);
  }));

  it('addMemberToTeam should open dialog', () => {
    paramMapSubject.next({ get: () => team1.id.toString() });
    fixture.detectChanges();

    component.dataSource.data = []; // Reset source explicitly for test
    dialogService.open.mockReturnValue({ afterClosed: () => of(null) });
    component.addMemberToTeam();

    expect(dialogService.open)
      .toHaveBeenCalledWith(AddMemberToTeamDialogComponent, expect.objectContaining({
        data: {
          team: component.selectedTeam(),
          currentUsersOfTeam: component.dataSource.filteredData
        }
      }));
  });

  it('edit team should open dialog', () => {
    paramMapSubject.next({ get: () => team1.id.toString() });
    fixture.detectChanges();

    dialogService.open.mockReturnValue({ afterClosed: () => of(null) });
    component.editTeam();

    expect(dialogService.open)
      .toHaveBeenCalledWith(AddEditTeamDialogComponent, expect.objectContaining({
        data: { team: component.selectedTeam() }
      }));
  });

  it('archiveTeam should open dialog, update markedAsArchivedAt, and call teamStateService if a quarter is selected', fakeAsync(() => {
    const teamToArchive = { ...team1,
      markedAsArchivedAt: null };
    const selectedQuarter = { startDate: new Date('2026-07-01') };

    (component as any).dialog = dialogService;
    dialogService.open.mockReturnValue({ afterClosed: () => of(selectedQuarter) });
    teamStateServiceMock.archiveTeam.mockReturnValue(of(null));

    component.archiveTeam(teamToArchive);
    tick();

    expect(dialogService.open)
      .toHaveBeenCalledWith(ArchiveTeamDialogComponent, expect.objectContaining({ panelClass: 'okr-dialog-panel-small' }));
    expect(teamToArchive.markedAsArchivedAt)
      .toBe(selectedQuarter.startDate);
    expect(teamStateServiceMock.archiveTeam)
      .toHaveBeenCalledTimes(1);
    expect(teamStateServiceMock.archiveTeam)
      .toHaveBeenCalledWith(teamToArchive);
  }));

  it('archiveTeam should not call teamStateService if dialog is canceled', fakeAsync(() => {
    const teamToArchive = { ...team1 };

    (component as any).dialog = dialogService;
    dialogService.open.mockReturnValue({ afterClosed: () => of(undefined) });

    component.archiveTeam(teamToArchive);
    tick();

    expect(teamStateServiceMock.archiveTeam)
      .toHaveBeenCalledTimes(0);
  }));

  it('unarchiveTeam should open confirm dialog, clear markedAsArchivedAt, and call teamStateService if confirmed', fakeAsync(() => {
    const teamToUnarchive = { ...team1,
      markedAsArchivedAt: new Date('2026-01-01') };

    dialogService.openConfirmDialog.mockReturnValue({ afterClosed: () => of(true) });
    teamStateServiceMock.unarchiveTeam.mockReturnValue(of(null));

    component.unarchiveTeam(teamToUnarchive);
    tick();

    expect(dialogService.openConfirmDialog)
      .toHaveBeenCalledWith('CONFIRMATION.UNARCHIVE.TEAM', expect.objectContaining({ team: teamToUnarchive.name }));
    expect(teamToUnarchive.markedAsArchivedAt)
      .toBeNull();
    expect(teamStateServiceMock.unarchiveTeam)
      .toHaveBeenCalledTimes(1);
    expect(teamStateServiceMock.unarchiveTeam)
      .toHaveBeenCalledWith(teamToUnarchive.id);
  }));

  it('unarchiveTeam should not call teamStateService if confirm dialog is canceled', fakeAsync(() => {
    const teamToUnarchive = { ...team1,
      markedAsArchivedAt: new Date('2026-01-01') };

    dialogService.openConfirmDialog.mockReturnValue({ afterClosed: () => of(false) });

    component.unarchiveTeam(teamToUnarchive);
    tick();

    expect(teamStateServiceMock.unarchiveTeam)
      .toHaveBeenCalledTimes(0);
    expect(teamToUnarchive.markedAsArchivedAt).not.toBeNull();
  }));

  describe('Computed Signal: showInviteMember', () => {
    it('should return true when no team is selected and user is OKR champion', () => {
      userServiceMock.getCurrentUser.mockReturnValue({ ...testUser,
        isOkrChampion: true });
      paramMapSubject.next({ get: () => null });
      fixture.detectChanges();

      expect(component.showInviteMember())
        .toBeTruthy();
    });

    it('should return false when a team is selected, even if user is OKR champion', () => {
      userServiceMock.getCurrentUser.mockReturnValue({ ...testUser,
        isOkrChampion: true });
      paramMapSubject.next({ get: () => team1.id.toString() });
      fixture.detectChanges();

      expect(component.showInviteMember())
        .toBeFalsy();
    });

    it('should return false when no team is selected but user is NOT OKR champion', () => {
      userServiceMock.getCurrentUser.mockReturnValue({ ...testUser,
        isOkrChampion: false });
      paramMapSubject.next({ get: () => null });
      fixture.detectChanges();

      expect(component.showInviteMember())
        .toBeFalsy();
    });
  });

  describe('Computed Signal: isTeamWriteable', () => {
    it('should return false if selectedTeam is undefined', () => {
      paramMapSubject.next({ get: () => null });
      fixture.detectChanges();

      expect(component.isTeamWriteable())
        .toBeFalsy();
    });

    it('should return false if selectedTeam is not writeable', () => {
      const teamCopy = { ...team1,
        isWriteable: false };
      teamStateServiceMock.getTeams.mockReturnValue(signal([teamCopy]));
      paramMapSubject.next({ get: () => teamCopy.id.toString() });
      fixture.detectChanges();

      expect(component.isTeamWriteable())
        .toBeFalsy();
    });

    it('should return true if selectedTeam is writeable', () => {
      const writeableTeam = { ...team1,
        isWriteable: true };

      mockTeamsSignal.set([writeableTeam]);
      fixture.detectChanges();

      expect(component.isTeamWriteable())
        .toBeTruthy();
    });
  });

  describe('Computed Signal: isTeamArchived', () => {
    it('should return false if selectedTeam is undefined', () => {
      paramMapSubject.next({ get: () => null });
      fixture.detectChanges();

      expect(component.isTeamArchived())
        .toBeFalsy();
    });

    it('should return false if team markedAsArchivedAt is null', () => {
      const teamCopy = { ...team1,
        markedAsArchivedAt: null };
      teamStateServiceMock.getTeams.mockReturnValue(signal([teamCopy]));
      paramMapSubject.next({ get: () => teamCopy.id.toString() });
      fixture.detectChanges();

      expect(component.isTeamArchived())
        .toBeFalsy();
    });

    it('should return true if team has a markedAsArchivedAt date', () => {
      const archivedTeam = { ...team1,
        markedAsArchivedAt: new Date('2026-06-01') };

      mockTeamsSignal.set([archivedTeam]);
      fixture.detectChanges();

      expect(component.isTeamArchived())
        .toBeTruthy();
    });
  });
});
