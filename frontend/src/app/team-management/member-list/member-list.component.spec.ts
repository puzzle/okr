import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import { of } from 'rxjs';
import { MemberListComponent } from './member-list.component';
import { UserService } from '../../services/user.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { User } from '../../shared/types/model/User';
import { team1, team2, testUser, users } from '../../shared/testData';
import { convertFromUser, convertFromUsers } from '../../shared/types/model/UserTableEntry';
import { UserRole } from '../../shared/types/enums/UserRole';

const userServiceMock = {
  getUsers: jest.fn(),
};

const activatedRouteMock = {
  paramMap: jest.fn(),
};

describe('MemberListComponent', () => {
  let component: MemberListComponent;
  let fixture: ComponentFixture<MemberListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MemberListComponent],
      imports: [HttpClientTestingModule],
      providers: [
        { provide: UserService, useValue: userServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        ChangeDetectorRef,
      ],
    });

    fixture = TestBed.createComponent(MemberListComponent);
    component = fixture.componentInstance;

    jest.spyOn(userServiceMock, 'getUsers').mockReturnValue(of([]));
    jest.spyOn(activatedRouteMock, 'paramMap').mockReturnValue(of(1));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should test method convertFromUser', () => {
    const user: User = testUser;
    let userTableEntry = convertFromUser(user);

    expect(userTableEntry.id).toBe(user.id);
    expect(userTableEntry.firstname).toBe(user.firstname);
    expect(userTableEntry.lastname).toBe(user.lastname);
    expect(userTableEntry.email).toBe(user.email);
    expect(userTableEntry.roles).toStrictEqual([UserRole.TEAM_MEMBER]);
    expect(userTableEntry.teams).toStrictEqual([team1.name]);

    testUser.userTeamList.push({
      id: 2,
      team: team2,
      isTeamAdmin: true,
    });
    testUser.isOkrChampion = true;

    userTableEntry = convertFromUser(user);

    expect(userTableEntry.roles).toStrictEqual([UserRole.OKR_CHAMPION, UserRole.TEAM_ADMIN, UserRole.TEAM_MEMBER]);
    expect(userTableEntry.teams).toStrictEqual([team1.name, team2.name]);
  });

  it('should test method convertFromUser throws error, if user is not in any team', () => {
    const user: User = testUser;
    user.userTeamList = [];
    expect(() => convertFromUser(user)).toThrowError('User should have at least one role');
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
});
