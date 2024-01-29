import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { MemberDetailComponent } from './member-detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { delay, of } from 'rxjs';
import { TranslateModule } from '@ngx-translate/core';
import { BrowserModule } from '@angular/platform-browser';
import { SharedModule } from '../../shared/shared.module';
import { UserService } from '../../services/user.service';
import { testUser } from '../../shared/testData';
import { AddUserTeamComponent } from '../add-user-team/add-user-team.component';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { TeamService } from '../../services/team.service';
import { ShowEditRoleComponent } from '../show-edit-role/show-edit-role.component';
import { PuzzleIconButtonComponent } from '../../shared/custom/puzzle-icon-button/puzzle-icon-button.component';
import { PuzzleIconComponent } from '../../shared/custom/puzzle-icon/puzzle-icon.component';
import { CommonModule } from '@angular/common';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

describe('MemberDetailComponent', () => {
  let component: MemberDetailComponent;
  let fixture: ComponentFixture<MemberDetailComponent>;

  const activatedRouteMock = {
    paramMap: of({
      get: (): any => 1,
    }),
  };

  const userServiceMock = {
    getUserById: jest.fn(),
    getCurrentUser: jest.fn(),
    reloadUsers: jest.fn(),
    reloadCurrentUser: jest.fn(),
  };

  const teamServiceMock = {
    removeUserFromTeam: jest.fn(),
    updateOrAddTeamMembership: jest.fn(),
    getAllTeams: () => of([]),
  };

  const dialogMock = {
    open: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        MemberDetailComponent,
        AddUserTeamComponent,
        ShowEditRoleComponent,
        PuzzleIconButtonComponent,
        PuzzleIconComponent,
      ],
      imports: [
        HttpClientTestingModule,
        TranslateModule.forRoot(),
        BrowserModule,
        SharedModule,
        MatTableModule,
        MatIconModule,
        CommonModule,
      ],
      providers: [
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: UserService, useValue: userServiceMock },
        { provide: TeamService, useValue: teamServiceMock },
        { provide: MatDialog, useValue: dialogMock },
      ],
      schemas: [
        NO_ERRORS_SCHEMA, //
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MemberDetailComponent);
    component = fixture.componentInstance;

    userServiceMock.getUserById.mockReturnValue(of(testUser));
    userServiceMock.getCurrentUser.mockReturnValue(testUser);
    userServiceMock.reloadCurrentUser.mockReturnValue(of(testUser));

    fixture.detectChanges();
  });

  afterEach(() => {
    userServiceMock.getUserById.mockReset();
    userServiceMock.reloadUsers.mockReset();
    teamServiceMock.updateOrAddTeamMembership.mockReset();
    dialogMock.open.mockReset();
    teamServiceMock.removeUserFromTeam.mockReset();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should set selectedUserIsLoggedInUser and currentUserTeams correctly', (done) => {
    component.ngOnInit();
    component.currentUserTeams$.subscribe((userTeams) => {
      expect(userTeams).toStrictEqual(testUser.userTeamList);
      expect(component.user).toStrictEqual(testUser);
      expect(component.selectedUserIsLoggedInUser).toBeTruthy();
      done();
    });
  });

  it('removeUserFromTeam should call removeUserFromTeam and loadUser', fakeAsync(() => {
    const user = testUser;
    const userTeam = testUser.userTeamList[0];
    teamServiceMock.removeUserFromTeam.mockReturnValue(of());
    userServiceMock.getUserById.mockReturnValue(of(user));
    dialogMock.open.mockReturnValue({
      afterClosed: () => of(true),
    });

    component.removeUserFromTeam(userTeam, user);
    tick();

    expect(teamServiceMock.removeUserFromTeam).toHaveBeenCalledTimes(1);
    expect(teamServiceMock.removeUserFromTeam).toHaveBeenCalledWith(user.id, userTeam.team);
    expect(userServiceMock.getUserById).toHaveBeenCalledWith(user.id);
  }));

  it('removeUserFromTeam should not call removeUserFromTeam if dialog canceled', fakeAsync(() => {
    const user = testUser;
    const userTeam = testUser.userTeamList[0];
    teamServiceMock.removeUserFromTeam.mockReturnValue(of());
    userServiceMock.getUserById.mockReturnValue(of(user));
    dialogMock.open.mockReturnValue({
      afterClosed: () => of(false),
    });

    component.removeUserFromTeam(userTeam, user);
    tick();

    expect(teamServiceMock.removeUserFromTeam).toHaveBeenCalledTimes(0);
  }));

  it('addTeamRole should call updateOrAddTeamMembership, loadUser, reloadUsers and set userTeamEditId to null', fakeAsync(() => {
    const user = testUser;
    const userTeam = testUser.userTeamList[0];

    teamServiceMock.updateOrAddTeamMembership.mockReturnValue(of(null));
    userServiceMock.getUserById.mockReturnValue(of(user));

    component.addTeamRole(userTeam, user);
    tick();

    expect(teamServiceMock.updateOrAddTeamMembership).toHaveBeenCalledTimes(1);
    expect(teamServiceMock.updateOrAddTeamMembership).toHaveBeenCalledWith(user.id, userTeam);
    expect(userServiceMock.getUserById).toHaveBeenCalledWith(user.id);
  }));

  it('updateTeamRole should call updateOrAddTeamMembership, loadUser, reloadUsers and set userTeamEditId to null', fakeAsync(() => {
    const user = testUser;
    const userTeam = testUser.userTeamList[0];

    teamServiceMock.updateOrAddTeamMembership.mockReturnValue(of(null));
    userServiceMock.getUserById.mockReturnValue(of(user));

    component.updateTeamRole(false, userTeam, user);
    tick();

    expect(teamServiceMock.updateOrAddTeamMembership).toHaveBeenCalledTimes(1);
    expect(teamServiceMock.updateOrAddTeamMembership).toHaveBeenCalledWith(user.id, userTeam);
    expect(userServiceMock.getUserById).toHaveBeenCalledWith(user.id);
  }));

  it('updateTeamRole should set isAdmin only after successfull request', fakeAsync(() => {
    const user = testUser;
    const userTeam = { ...testUser.userTeamList[0] };
    userTeam.isTeamAdmin = false;

    teamServiceMock.updateOrAddTeamMembership.mockReturnValue(of(null).pipe(delay(10)));
    userServiceMock.getUserById.mockReturnValue(of(user));

    component.updateTeamRole(true, userTeam, user);

    expect(userTeam.isTeamAdmin).toBeFalsy();
    tick(11);
    expect(userTeam.isTeamAdmin).toBeTruthy();
  }));
});
