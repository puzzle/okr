import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UserService } from '../../services/user.service';
import { DeleteUserComponent } from './delete-user.component';
import { of, Subject } from 'rxjs';
import { testUser } from '../../shared/testData';
import { MatDialogRef } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../shared/dialog/confirm-dialog/confirm-dialog.component';
import { DialogService } from '../../services/dialog.service';
import { Location } from '@angular/common';

describe('DeleteUserComponent', () => {
  let component: DeleteUserComponent;
  let fixture: ComponentFixture<DeleteUserComponent>;

  const dialogRefMock: jest.Mocked<MatDialogRef<ConfirmDialogComponent>> = { afterClosed: jest.fn() } as any;
  const userServiceMock = {
    deleteUser: jest.fn(),
    getOrInitCurrentUser: jest.fn(), // the okrUser
    reloadUsers: jest.fn(),
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
    userServiceMock.getOrInitCurrentUser.mockReturnValue(of(testUser));
    fixture.detectChanges();
  });

  afterEach(() => {
    userServiceMock.reloadUsers.mockReset();
    mockLocation.back.mockReset();
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
});
