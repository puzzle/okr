import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { MemberListTableComponent } from './member-list-table.component';
import { team1, testUser } from '../../../shared/test-data';
import { of } from 'rxjs';
import { UserTableEntry } from '../../../shared/types/model/user-table-entry';
import { UserService } from '../../../services/user.service';
import { TeamStateService } from '../../../services/team.state.service';
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

  const teamStateServiceMock = {
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
      { provide: TeamStateService,
        useValue: teamStateServiceMock }, // Updated Token
      { provide: DialogService,
        useValue: dialogService }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MemberListTableComponent);
    component = fixture.componentInstance;

    teamStateServiceMock.removeUserFromTeam.mockReset();
    teamStateServiceMock.updateOrAddTeamMembership.mockReset();
    userServiceMock.reloadUsers.mockReset();
    userServiceMock.reloadCurrentUser.mockReset();

    fixture.detectChanges();
  });

  afterEach(() => {
    teamStateServiceMock.removeUserFromTeam.mockReset();
    teamStateServiceMock.updateOrAddTeamMembership.mockReset();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should set displayedColumns for all teams correctly', fakeAsync(() => {
    fixture.componentRef.setInput('currentTeam', undefined); // Fixed Input Name
    tick();
    expect(component.displayedColumns())
      .toStrictEqual([
        'icon',
        'name',
        'roles',
        'teams',
        'okr_champion'
      ]);
  }));

  it('should set displayedColumns for admin team correctly', fakeAsync(() => {
    fixture.componentRef.setInput('currentTeam', team1); // Fixed Input Name
    tick();
    expect(component.displayedColumns())
      .toStrictEqual(['icon',
        'name',
        'role']);
  }));

  it('should set displayedColumns for admin team correctly if writeable', fakeAsync(() => {
    const team = { ...team1,
      isWriteable: true };
    fixture.componentRef.setInput('currentTeam', team); // Fixed Input Name
    tick();
    expect(component.displayedColumns())
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
    const entry = { id: 1 };
    fixture.componentRef.setInput('currentTeam', team1); // Fixed Input Name
    teamStateServiceMock.removeUserFromTeam.mockReturnValue(of(null));
    userServiceMock.reloadUsers.mockReturnValue(of());
    userServiceMock.reloadCurrentUser.mockReturnValue(of());
    dialogService.openConfirmDialog.mockReturnValue({ afterClosed: () => of(true) });

    component.removeMemberFromTeam(entry as UserTableEntry, new MouseEvent('click'));
    tick();

    expect(teamStateServiceMock.removeUserFromTeam)
      .toHaveBeenCalledTimes(1);
    expect(teamStateServiceMock.removeUserFromTeam)
      .toHaveBeenCalledWith(entry.id, component.currentTeam());
    expect(userServiceMock.reloadUsers)
      .toHaveBeenCalledTimes(1);
    expect(userServiceMock.reloadCurrentUser)
      .toHaveBeenCalledTimes(1);
  }));

  it('removeMemberFromTeam should not call removeUserFromTeam and reloadUsers if not confirmed', fakeAsync(() => {
    const entry = { id: 1 };
    fixture.componentRef.setInput('currentTeam', team1); // Fixed Input Name
    teamStateServiceMock.removeUserFromTeam.mockReturnValue(of(null));
    userServiceMock.reloadUsers.mockReturnValue(of());
    userServiceMock.reloadCurrentUser.mockReturnValue(of());
    dialogService.openConfirmDialog.mockReturnValue({ afterClosed: () => of(false) });

    component.removeMemberFromTeam(entry as UserTableEntry, new MouseEvent('click'));
    tick();

    expect(teamStateServiceMock.removeUserFromTeam)
      .toHaveBeenCalledTimes(0);
    expect(userServiceMock.reloadUsers)
      .toHaveBeenCalledTimes(0);
    expect(userServiceMock.reloadCurrentUser)
      .toHaveBeenCalledTimes(0);
  }));

  it('saveUserTeamRole should call updateOrAddTeamMembership and reload users', fakeAsync(() => {
    teamStateServiceMock.updateOrAddTeamMembership.mockReturnValue(of(null));
    userServiceMock.reloadCurrentUser.mockReturnValue(of());
    const entry = { id: 1 } as any;
    const ut = testUser.userTeamList[0];

    component.saveUserTeamMembership(true, entry, ut);
    tick();

    expect(teamStateServiceMock.updateOrAddTeamMembership)
      .toHaveBeenCalledWith(entry.id, ut);
    expect(userServiceMock.reloadUsers)
      .toHaveBeenCalledTimes(1);
    expect(userServiceMock.reloadCurrentUser)
      .toHaveBeenCalledTimes(1);
  }));

  it('getSingleUserTeam should return first userTeam of userTableEntry', () => {
    const ut = { userTeamList: [testUser.userTeamList[0]] } as any;
    expect(component.getSingleUserTeam(ut))
      .toStrictEqual(testUser.userTeamList[0]);
  });

  it('getSingleUserTeam should throw error if userTeamList.length is not 1', () => {
    const ut = { userTeamList: [testUser.userTeamList[0],
      testUser.userTeamList[0]] } as any;
    expect(() => component.getSingleUserTeam(ut))
      .toThrow('it should have exactly one UserTeam at this point');
    ut.userTeamList = [];
    expect(() => component.getSingleUserTeam(ut))
      .toThrow('it should have exactly one UserTeam at this point');
  });

  describe('isTeamWriteable', () => {
    it('should return false if selectedTeam is undefined', () => {
      fixture.componentRef.setInput('currentTeam', undefined); // Fixed Input Name
      expect(component.isTeamWriteable())
        .toBe(false);
    });

    it('should return false if selectedTeam is not writeable', () => {
      const team = { ...team1,
        isWriteable: false };
      fixture.componentRef.setInput('currentTeam', team); // Fixed Input Name
      expect(component.isTeamWriteable())
        .toBe(false);
    });

    it('should return true if selectedTeam is writeable', () => {
      const team = { ...team1,
        isWriteable: true };
      fixture.componentRef.setInput('currentTeam', team); // Fixed Input Name
      expect(component.isTeamWriteable())
        .toBe(true);
    });
  });

  describe('isTeamArchived', () => {
    it('should return false if selectedTeam is undefined', () => {
      fixture.componentRef.setInput('currentTeam', undefined); // Fixed Input Name
      expect(component.isTeamArchived())
        .toBe(false);
    });

    it('should return false if team markedAsArchivedAt is null', () => {
      const team = { ...team1,
        markedAsArchivedAt: null };
      fixture.componentRef.setInput('currentTeam', team); // Fixed Input Name
      expect(component.isTeamArchived())
        .toBe(false);
    });

    it('should return true if team has a markedAsArchivedAt date', () => {
      const team = { ...team1,
        markedAsArchivedAt: new Date('2026-06-01') };
      fixture.componentRef.setInput('currentTeam', team); // Fixed Input Name
      expect(component.isTeamArchived())
        .toBe(true);
    });
  });
});
