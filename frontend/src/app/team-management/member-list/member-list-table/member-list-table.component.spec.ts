import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';

import { MemberListTableComponent } from './member-list-table.component';
import { team1, testUser } from '../../../shared/testData';
import { BehaviorSubject, of } from 'rxjs';
import { UserTableEntry } from '../../../shared/types/model/UserTableEntry';
import { UserService } from '../../../services/user.service';
import { TeamService } from '../../../services/team.service';
import { Team } from '../../../shared/types/model/Team';
import { MatTableModule } from '@angular/material/table';
import { DialogService } from '../../../services/dialog.service';

describe('MemberListTableComponent', () => {
  let component: MemberListTableComponent;
  let fixture: ComponentFixture<MemberListTableComponent>;

  const userServiceMock = {
    getUsers: jest.fn(),
    reloadUsers: jest.fn(),
    reloadCurrentUser: jest.fn()
  };

  const teamServiceMock = {
    getAllTeams: jest.fn(),
    deleteTeam: jest.fn(),
    removeUserFromTeam: jest.fn(),
    updateOrAddTeamMembership: jest.fn()
  };

  const dialogService = {
    open: jest.fn(),
    openConfirmDialog: jest.fn()
  };

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [MatTableModule],
      declarations: [MemberListTableComponent],
      providers: [{ provide: UserService,
        useValue: userServiceMock },
      { provide: TeamService,
        useValue: teamServiceMock },
      { provide: DialogService,
        useValue: dialogService }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MemberListTableComponent);

    component = fixture.componentInstance;

    component.selectedTeam$ = new BehaviorSubject<Team | undefined>(undefined);
    teamServiceMock.removeUserFromTeam.mockReset();
    userServiceMock.reloadUsers.mockReset();
    userServiceMock.reloadCurrentUser.mockReset();

    fixture.detectChanges();
  });

  afterEach(() => {
    teamServiceMock.removeUserFromTeam.mockReset();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should set displayedColumns for all teams correctly', fakeAsync(() => {
    component.selectedTeam$.next(undefined);
    tick();
    expect(component.displayedColumns)
      .toStrictEqual([
        'icon',
        'name',
        'roles',
        'teams',
        'okr_champion'
      ]);
  }));

  it('should set displayedColumns for admin team correctly', fakeAsync(() => {
    component.selectedTeam$.next(team1);
    tick();
    expect(component.displayedColumns)
      .toStrictEqual(['icon',
        'name',
        'role']);
  }));

  it('should set displayedColumns for admin team correctly', fakeAsync(() => {
    const team = { ...team1 };
    team.writeable = true;
    component.selectedTeam$.next(team);
    tick();
    expect(component.displayedColumns)
      .toStrictEqual([
        'icon',
        'name',
        'role',
        'menu'
      ]);
  }));

  it('should return correct memberDetailsLink', () => {
    expect(component.getMemberDetailsLink(testUser))
      .toStrictEqual('/team-management/details/member/' + testUser.id);
  });

  it('removeMemberFromTeam should call removeUserFromTeam and reloadUsers if confirmed', fakeAsync(() => {
    const entry = {
      id: 1
    };
    component.selectedTeam$.next(team1);
    teamServiceMock.removeUserFromTeam.mockReturnValue(of(null));
    userServiceMock.reloadUsers.mockReturnValue(of());
    userServiceMock.reloadCurrentUser.mockReturnValue(of());
    dialogService.openConfirmDialog.mockReturnValue({
      afterClosed: () => of(true)
    });

    component.removeMemberFromTeam(entry as UserTableEntry, new MouseEvent('click'));
    tick();

    expect(teamServiceMock.removeUserFromTeam)
      .toBeCalledTimes(1);
    expect(teamServiceMock.removeUserFromTeam)
      .toBeCalledWith(entry.id, component.selectedTeam$.value);
    expect(userServiceMock.reloadUsers)
      .toBeCalledTimes(1);
    expect(userServiceMock.reloadCurrentUser)
      .toBeCalledTimes(1);
  }));

  it('removeMemberFromTeam should not call removeUserFromTeam and reloadUsers if not confirmed', fakeAsync(() => {
    const entry = {
      id: 1
    };
    component.selectedTeam$.next(team1);
    teamServiceMock.removeUserFromTeam.mockReturnValue(of(null));
    userServiceMock.reloadUsers.mockReturnValue(of());
    userServiceMock.reloadCurrentUser.mockReturnValue(of());
    dialogService.openConfirmDialog.mockReturnValue({
      afterClosed: () => of(false)
    });

    component.removeMemberFromTeam(entry as UserTableEntry, new MouseEvent('click'));
    tick();

    expect(teamServiceMock.removeUserFromTeam)
      .toBeCalledTimes(0);
    expect(userServiceMock.reloadUsers)
      .toBeCalledTimes(0);
    expect(userServiceMock.reloadCurrentUser)
      .toBeCalledTimes(0);
  }));

  it('saveUserTeamRole should call updateOrAddTeamMembership and reload users', fakeAsync(() => {
    teamServiceMock.updateOrAddTeamMembership.mockReturnValue(of(null));
    userServiceMock.reloadCurrentUser.mockReturnValue(of());
    const entry = {
      id: 1
    } as any;
    const ut = testUser.userTeamList[0];
    component.saveUserTeamMembership(true, entry, ut);
    tick();
    expect(teamServiceMock.updateOrAddTeamMembership)
      .toHaveBeenCalledWith(entry.id, ut);
    expect(userServiceMock.reloadUsers)
      .toHaveBeenCalledTimes(1);
    expect(userServiceMock.reloadCurrentUser)
      .toHaveBeenCalledTimes(1);
  }));

  it('getSingleUserTeam should return first userTeam uf userTableEntry', () => {
    const ut = {
      userTeamList: [testUser.userTeamList[0]]
    } as any;
    expect(component.getSingleUserTeam(ut))
      .toStrictEqual(testUser.userTeamList[0]);
  });

  it('getSingleUserTeam should throw error if userTeamList.length is not 1', () => {
    const ut = {
      userTeamList: [testUser.userTeamList[0],
        testUser.userTeamList[0]]
    } as any;
    expect(() => component.getSingleUserTeam(ut))
      .toThrowError('it should have exactly one UserTeam at this point');
    ut.userTeamList = [];
    expect(() => component.getSingleUserTeam(ut))
      .toThrowError('it should have exactly one UserTeam at this point');
  });
});
