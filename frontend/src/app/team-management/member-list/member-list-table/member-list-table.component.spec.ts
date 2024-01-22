import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';

import { MemberListTableComponent } from './member-list-table.component';
import { team1, testUser } from '../../../shared/testData';
import { BehaviorSubject, of } from 'rxjs';
import { UserTableEntry } from '../../../shared/types/model/UserTableEntry';
import { UserService } from '../../../services/user.service';
import { TeamService } from '../../../services/team.service';
import { Team } from '../../../shared/types/model/Team';
import { MatTableModule } from '@angular/material/table';

describe('MemberListTableComponent', () => {
  let component: MemberListTableComponent;
  let fixture: ComponentFixture<MemberListTableComponent>;

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

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MatTableModule],
      declarations: [MemberListTableComponent],
      providers: [
        { provide: UserService, useValue: userServiceMock },
        { provide: TeamService, useValue: teamServiceMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MemberListTableComponent);

    component = fixture.componentInstance;

    component.selectedTeam$ = new BehaviorSubject<Team | undefined>(undefined);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set displayedColumns for all teams correctly', fakeAsync(() => {
    component.selectedTeam$.next(undefined);
    tick();
    expect(component.displayedColumns).toStrictEqual(['icon', 'name', 'roles', 'teams']);
  }));

  it('should set displayedColumns for admin team correctly', fakeAsync(() => {
    component.selectedTeam$.next(team1);
    tick();
    expect(component.displayedColumns).toStrictEqual(['icon', 'name', 'roles']);
  }));

  it('should set displayedColumns for admin team correctly', fakeAsync(() => {
    const team = { ...team1 };
    team.isWriteable = true;
    component.selectedTeam$.next(team);
    tick();
    expect(component.displayedColumns).toStrictEqual(['icon', 'name', 'roles', 'delete']);
  }));

  it('should return correct memberDetailsLink', () => {
    expect(component.getMemberDetailsLink(testUser)).toStrictEqual('/team-management/details/member/' + testUser.id);
  });

  it('removeMemberFromTeam should call removeUserFromTeam and reloadUsers', fakeAsync(() => {
    const entry = {
      id: 1,
    };
    component.selectedTeam$.next(team1);
    teamServiceMock.removeUserFromTeam.mockReturnValue(of(null));
    userServiceMock.reloadUsers.mockReturnValue(of());
    userServiceMock.reloadCurrentUser.mockReturnValue(of());

    component.removeMemberFromTeam(entry as UserTableEntry, new MouseEvent('click'));
    tick();

    expect(teamServiceMock.removeUserFromTeam).toBeCalledTimes(1);
    expect(teamServiceMock.removeUserFromTeam).toBeCalledWith(entry.id, component.selectedTeam$.value);
    expect(userServiceMock.reloadUsers).toBeCalledTimes(1);
    expect(userServiceMock.reloadCurrentUser).toBeCalledTimes(1);
  }));
});
