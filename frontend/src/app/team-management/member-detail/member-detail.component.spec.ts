import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { MemberDetailComponent } from './member-detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { SharedModule } from '../../shared/shared.module';
import { UserService } from '../../services/user.service';
import { testUser } from '../../shared/testData';
import { AddUserTeamComponent } from '../add-user-team/add-user-team.component';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { TeamService } from '../../services/team.service';
import spyOn = jest.spyOn;

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

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MemberDetailComponent, AddUserTeamComponent],
      imports: [
        HttpClientTestingModule,
        TranslateModule.forRoot(),
        CommonModule,
        BrowserModule,
        SharedModule,
        MatTableModule,
        MatIconModule,
      ],
      providers: [
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: UserService, useValue: userServiceMock },
        { provide: TeamService, useValue: teamServiceMock },
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

    component.removeUserFromTeam(userTeam, user);
    tick();

    expect(teamServiceMock.removeUserFromTeam).toHaveBeenCalledTimes(1);
    expect(teamServiceMock.removeUserFromTeam).toHaveBeenCalledWith(user.id, userTeam.team);
    expect(userServiceMock.getUserById).toHaveBeenCalledWith(user.id);
  }));

  it('saveTeamRole should call updateOrAddTeamMembership, loadUser, reloadUsers and set userTeamEditId to null', fakeAsync(() => {
    const user = testUser;
    const userTeam = testUser.userTeamList[0];

    teamServiceMock.updateOrAddTeamMembership.mockReturnValue(of(null));
    userServiceMock.getUserById.mockReturnValue(of(user));

    component.saveTeamRole(userTeam, user);
    tick();

    expect(teamServiceMock.updateOrAddTeamMembership).toHaveBeenCalledTimes(1);
    expect(teamServiceMock.updateOrAddTeamMembership).toHaveBeenCalledWith(user, userTeam);
    expect(userServiceMock.getUserById).toHaveBeenCalledWith(user.id);
  }));

  it('saveIsAdmin should set admin and call saveTeamRole', () => {
    const user = testUser;
    const userTeam = testUser.userTeamList[0];
    const spy = spyOn(component, 'saveIsAdmin');

    teamServiceMock.updateOrAddTeamMembership.mockReturnValue(of(' '));

    userTeam.isTeamAdmin = false;
    component.saveIsAdmin(userTeam, user, true);
    expect(userTeam.isTeamAdmin).toBeTruthy();
    expect(spy).toBeCalledTimes(1);
  });
});
