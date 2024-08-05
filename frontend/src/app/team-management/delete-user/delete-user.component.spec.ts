import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UserService } from '../../services/user.service';
import { team1 } from '../../shared/testData';
import { DeleteUserComponent } from './delete-user.component';
import { User } from '../../shared/types/model/User';
import { of } from 'rxjs';

export const testUserWithTeam: User = {
  id: 1,
  firstname: 'Bob',
  lastname: 'Baumeister',
  isOkrChampion: false,
  userTeamList: [
    {
      id: 1,
      team: team1,
      isTeamAdmin: false,
    },
  ],
  email: 'bob.baumeister@puzzle.ch',
};

export const testUserWithoutTeam: User = {
  id: 2,
  firstname: 'Hans',
  lastname: 'Muster',
  isOkrChampion: false,
  userTeamList: [],
  email: 'hans.muster@puzzle.ch',
};

describe('DeleteUserComponent', () => {
  let component: DeleteUserComponent;
  let fixture: ComponentFixture<DeleteUserComponent>;

  const userServiceMock = {
    deleteUser: jest.fn(),
  };

  const matDialogMock = {
    close: jest.fn(),
    open: jest.fn(),
  };

  const matDialogRefMock = {
    afterClosed: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [],
      imports: [],
      providers: [{ provide: UserService, useValue: userServiceMock }],
    }).compileComponents();

    fixture = TestBed.createComponent(DeleteUserComponent);
    component = fixture.componentInstance;
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should return true if user is in a team', () => {
    component.user = testUserWithTeam;
    expect(component.isUserMemberOfTeams()).toBe(true);
  });

  it('should return false if user it not in a team', () => {
    component.user = testUserWithoutTeam;
    expect(component.isUserMemberOfTeams()).toBe(false);
  });

  it('should return false userOkrData is undefined', () => {
    component.user = testUserWithoutTeam;
    component.userOkrData = undefined;
    expect(component.isUserOwnerOfKeyResults()).toBe(false);
  });

  it('should return true if userOkrData has no keyResults', () => {
    component.user = testUserWithoutTeam;
    component.userOkrData = {
      keyResults: [],
    };

    expect(component.isUserOwnerOfKeyResults()).toBe(false);
  });

  it('should return true if user is owner of keyResults', () => {
    component.user = testUserWithoutTeam;
    component.userOkrData = {
      keyResults: [
        {
          keyResultId: 100,
          keyResultName: 'keyResult1',
          objectiveId: 200,
          objectiveName: 'objective1',
        },
      ],
    };

    expect(component.isUserOwnerOfKeyResults()).toBe(true);
  });

  it('should delete user if user is not in a team and user has no keyResults', () => {
    component.user = testUserWithoutTeam;
    component.userOkrData = {
      keyResults: [],
    };
    matDialogMock.open.mockReturnValue(matDialogRefMock);
    matDialogRefMock.afterClosed.mockReturnValue(of());
    userServiceMock.deleteUser.mockReturnValue(of());

    component.deleteUser();

    setTimeout(() => expect(userServiceMock.deleteUser).toHaveBeenCalled(), 100);
  });
});
