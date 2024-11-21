import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UserService } from '../../services/user.service';
import { DeleteUserComponent } from './delete-user.component';
import { of, Subject } from 'rxjs';
import { testUser } from '../../shared/testData';
import { MatDialogRef } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../shared/dialog/confirm-dialog/confirm-dialog.component';
import { DialogService } from '../../services/dialog.service';
import { Location } from '@angular/common';
import { ButtonState } from '../../shared/types/enums/ButtonState';

describe('DeleteUserComponent', () => {
  let component: DeleteUserComponent;
  let fixture: ComponentFixture<DeleteUserComponent>;

  const dialogRefMock: jest.Mocked<MatDialogRef<ConfirmDialogComponent>> = { afterClosed: jest.fn() } as any;
  const userServiceMock = {
    deleteUser: jest.fn(),
    getOrInitCurrentUser: jest.fn(), // the okrUser
    reloadUsers: jest.fn(),
    getUserOkrData: jest.fn(),
    isUserMemberOfTeams: jest.fn(),
  };
  const dialogServiceMock = { openCustomizedConfirmDialog: jest.fn() };
  const mockLocation: jest.Mocked<Location> = { back: jest.fn() } as any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DeleteUserComponent],
      providers: [
        { provide: UserService, useValue: userServiceMock },
        { provide: DialogService, useValue: dialogServiceMock },
        { provide: Location, useValue: mockLocation },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteUserComponent);
    component = fixture.componentInstance;
    component.user = {
      id: 2,
      firstname: 'Hans',
      lastname: 'Muster',
      isOkrChampion: false,
      userTeamList: [],
      email: 'hans.muster@puzzle.ch',
    };
    component.currentTeams$ = new Subject();
    userServiceMock.getOrInitCurrentUser.mockReturnValue(of(testUser));
    userServiceMock.getUserOkrData.mockReturnValue(of({ keyResults: [] }));
    userServiceMock.isUserMemberOfTeams.mockReturnValue(of(true));
    fixture.detectChanges();
  });

  afterEach(() => {
    userServiceMock.reloadUsers.mockReset();
    mockLocation.back.mockReset();
    dialogServiceMock.openCustomizedConfirmDialog.mockReset();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('deleteUser() should delete user and reload users', () => {
    // arrange
    userServiceMock.deleteUser.mockReturnValue(of(userServiceMock.deleteUser));
    dialogRefMock.afterClosed.mockReturnValue(of('some data'));
    dialogServiceMock.openCustomizedConfirmDialog.mockReturnValue(dialogRefMock);

    // act
    component.deleteUser();

    // assert
    setTimeout(() => expect(userServiceMock.deleteUser).toHaveBeenCalledWith(component.user), 0);
    expect(userServiceMock.reloadUsers).toHaveBeenCalledTimes(1);
    expect(mockLocation.back).toHaveBeenCalledTimes(1);
  });

  it('deleteUser() should not reload users when UserService throws an error', () => {
    // arrange
    function createErrorSubject() {
      const myError = new Subject<any>();
      myError.error('uups');
      return myError;
    }

    userServiceMock.deleteUser.mockReturnValue(createErrorSubject());
    dialogRefMock.afterClosed.mockReturnValue(of('some data'));
    dialogServiceMock.openCustomizedConfirmDialog.mockReturnValue(dialogRefMock);

    // act
    component.deleteUser();

    // assert
    setTimeout(() => expect(userServiceMock.deleteUser).toHaveBeenCalledWith(component.user), 0);
    expect(userServiceMock.reloadUsers).toHaveBeenCalledTimes(0);
    expect(mockLocation.back).toHaveBeenCalledTimes(0);
  });

  it('deleteUserWithChecks() should not delete user which is member of a Team', () => {
    // arrange (user is member of team Lorem)
    component.userIsMemberOfTeams = true;
    component.user.userTeamList = [
      {
        id: 100,
        team: {
          id: 1,
          version: 2,
          name: 'Lorem',
          writeable: true,
        },
        isTeamAdmin: false,
      },
    ];

    // act
    component.deleteUserWithChecks();

    // assert
    expect(dialogServiceMock.openCustomizedConfirmDialog).toHaveBeenCalledTimes(1);
    expect(dialogServiceMock.openCustomizedConfirmDialog).toHaveBeenCalledWith({
      title: 'User kann nicht gelöscht werden',
      text: 'Hans Muster ist in folgenden Teams und kann daher nicht gelöscht werden: Lorem',
      yesButtonState: ButtonState.Hidden,
      noButtonState: ButtonState.Hidden,
      closeButtonState: ButtonState.VisibleEnabled,
    });
  });

  it('deleteUserWithChecks() should not delete user which has KeyResults', () => {
    // arrange (user has KeyResult one)
    component.userIsMemberOfTeams = false;
    component.userOkrData = {
      keyResults: [
        {
          keyResultId: 1,
          keyResultName: 'one',
          objectiveId: 2,
          objectiveName: 'two',
        },
      ],
    };

    // act
    component.deleteUserWithChecks();

    // assert
    expect(dialogServiceMock.openCustomizedConfirmDialog).toHaveBeenCalledTimes(1);
    expect(dialogServiceMock.openCustomizedConfirmDialog).toHaveBeenCalledWith({
      title: 'User kann nicht gelöscht werden',
      text: 'Hans Muster ist Owner folgender KeyResults und kann daher nicht gelöscht werden: \n\none\n(Objective: two)',
      yesButtonState: ButtonState.Hidden,
      noButtonState: ButtonState.Hidden,
      closeButtonState: ButtonState.VisibleEnabled,
    });
  });

  it('deleteUserWithChecks() should delete user which is not member of a Team and has no KeyResults', () => {
    // arrange
    component.userIsMemberOfTeams = false;
    component.userOkrData = { keyResults: [] };

    userServiceMock.deleteUser.mockReturnValue(of(userServiceMock.deleteUser));
    dialogRefMock.afterClosed.mockReturnValue(of('some data'));
    dialogServiceMock.openCustomizedConfirmDialog.mockReturnValue(dialogRefMock);

    // act
    component.deleteUserWithChecks();

    // assert
    expect(dialogServiceMock.openCustomizedConfirmDialog).toHaveBeenCalledTimes(1);
    expect(dialogServiceMock.openCustomizedConfirmDialog).toHaveBeenCalledWith({
      title: 'User löschen',
      text: 'Möchtest du den User Hans Muster wirklich löschen?',
      yesButtonState: ButtonState.VisibleEnabled,
      noButtonState: ButtonState.VisibleEnabled,
      closeButtonState: ButtonState.Hidden,
    });
  });

  it('updateUserTeamsStatusWhenTeamOfUserChanges() does not update userIsMemberOfTeams property if currentTeams$ does not emit a value ', () => {
    // arrange (currentTeams$ does not emit a value)
    component.currentTeams$ = of();

    // pre-condition
    expect(component.userIsMemberOfTeams).toBe(undefined);

    // act
    component.updateUserTeamsStatusWhenTeamOfUserChanges();

    // assert
    expect(component.userIsMemberOfTeams).toBe(undefined);
  });

  it('updateUserTeamsStatusWhenTeamOfUserChanges() does update userIsMemberOfTeams property if currentTeams$ does emit a value ', () => {
    // arrange (currentTeams$ does emit a value)
    component.currentTeams$ = of([
      {
        id: 100,
        team: {
          id: 1,
          version: 2,
          name: 'Lorem',
          writeable: true,
        },
        isTeamAdmin: false,
      },
    ]);

    // pre-condition
    expect(component.userIsMemberOfTeams).toBe(undefined);

    // act
    component.updateUserTeamsStatusWhenTeamOfUserChanges();

    // assert
    expect(component.userIsMemberOfTeams).toBe(true);
  });
});
