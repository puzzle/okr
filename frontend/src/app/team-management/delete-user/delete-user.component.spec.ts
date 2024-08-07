import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { UserService } from '../../services/user.service';
import { DeleteUserComponent } from './delete-user.component';
import { of } from 'rxjs';
import { UserOkrData } from '../../shared/types/model/UserOkrData';

describe('DeleteUserComponent', () => {
  let component: DeleteUserComponent;
  let fixture: ComponentFixture<DeleteUserComponent>;

  const userServiceMock = {
    deleteUser: jest.fn(),
    isUserMemberOfTeams: jest.fn(),
    getUserOkrData: jest.fn(),
  };

  const matDialogMock = {
    close: jest.fn(),
    open: jest.fn(),
  };

  const matDialogRefMock = {
    afterClosed: jest.fn(),
  };

  beforeEach(async () => {
    jest.resetAllMocks();
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

  it('should load UserOkrData and MemberTeamStatus (case is member of teams + with KeyResults)', fakeAsync(() => {
    const userOkrData: UserOkrData = {
      keyResults: [
        {
          keyResultId: 100,
          keyResultName: 'keyResult1',
          objectiveId: 200,
          objectiveName: 'objective1',
        },
      ],
    };

    component.currentTeams$ = of([]);
    userServiceMock.getUserOkrData.mockReturnValue(of(userOkrData));
    userServiceMock.isUserMemberOfTeams.mockReturnValue(of(true));

    component.ngOnInit();
    tick();

    expect(component.userOkrData).toBe(userOkrData);
    expect(component.userIsMemberOfTeams).toBe(true);
  }));

  it('should load UserOkrData and MemberTeamStatus (case not member of teams + no KeyResults)', fakeAsync(() => {
    const userOkrDataWithoutKeyResults: UserOkrData = {
      keyResults: [],
    };

    component.currentTeams$ = of([]);
    userServiceMock.getUserOkrData.mockReturnValue(of(userOkrDataWithoutKeyResults));
    userServiceMock.isUserMemberOfTeams.mockReturnValue(of(false));

    component.ngOnInit();
    tick();

    expect(component.userOkrData).toBe(userOkrDataWithoutKeyResults);
    expect(component.userIsMemberOfTeams).toBe(false);
  }));

  it('should return true if userIsMemberOfTeams is undefined', () => {
    component.userIsMemberOfTeams = undefined;
    expect(component.isUserMemberOfTeams()).toBe(true);
  });

  it('should return true if userIsMemberOfTeams is true', () => {
    component.userIsMemberOfTeams = true;
    expect(component.isUserMemberOfTeams()).toBe(true);
  });

  it('should return false if userIsMemberOfTeams is false', () => {
    component.userIsMemberOfTeams = false;
    expect(component.isUserMemberOfTeams()).toBe(false);
  });

  it('should return true if userOkrData is undefined', () => {
    component.userOkrData = undefined;
    expect(component.isUserOwnerOfKeyResults()).toBe(true);
  });

  it('should return false if userOkrData has no keyResults', () => {
    component.userOkrData = {
      keyResults: [],
    };

    expect(component.isUserOwnerOfKeyResults()).toBe(false);
  });

  it('should return true if user is owner of keyResults', () => {
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

  it('should not delete user when user is member of teams', () => {
    component.user = {
      id: 2,
      firstname: 'Hans',
      lastname: 'Muster',
      isOkrChampion: false,
      userTeamList: [],
      email: 'hans.muster@puzzle.ch',
    };
    component.userIsMemberOfTeams = true;

    component.deleteUser();

    expect(userServiceMock.deleteUser).not.toHaveBeenCalledWith(component.user);
  });

  it('should not delete user when user is owner of keyResults', () => {
    component.user = {
      id: 2,
      firstname: 'Hans',
      lastname: 'Muster',
      isOkrChampion: false,
      userTeamList: [],
      email: 'hans.muster@puzzle.ch',
    };
    component.userIsMemberOfTeams = false;
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

    component.deleteUser();

    expect(userServiceMock.deleteUser).not.toHaveBeenCalledWith(component.user);
  });

  it('should delete user if user is not in a team and user has no keyResults', () => {
    component.user = {
      id: 2,
      firstname: 'Hans',
      lastname: 'Muster',
      isOkrChampion: false,
      userTeamList: [],
      email: 'hans.muster@puzzle.ch',
    };
    component.userOkrData = {
      keyResults: [],
    };
    component.userIsMemberOfTeams = false;
    matDialogMock.open.mockReturnValue(matDialogRefMock);
    matDialogRefMock.afterClosed.mockReturnValue(of());
    userServiceMock.deleteUser.mockReturnValue(of());

    component.deleteUser();

    setTimeout(() => expect(userServiceMock.deleteUser).toHaveBeenCalledWith(component.user), 100);
  });
});
