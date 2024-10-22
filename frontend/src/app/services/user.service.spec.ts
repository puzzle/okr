import { fakeAsync, TestBed, tick } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UserService } from './user.service';
import { testUser, users } from '../shared/testData';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('getUsers should only reload users when they are not loaded yet', (done) => {
    const spy = jest.spyOn(service, 'reloadUsers');
    service.getUsers().subscribe(() => {
      expect(spy).toBeCalledTimes(1);
      httpMock.expectOne('api/v1/users');
      service.getUsers().subscribe((users) => {
        expect(spy).toBeCalledTimes(1);
        expect(users).toStrictEqual([]);
        done();
      });
    });
  });

  it('get current user should throw error, when not loaded', () => {
    expect(() => service.getCurrentUser()).toThrowError('user should not be undefined here');
  });

  it('init current user should load user', (done) => {
    expect(() => service.getCurrentUser()).toThrowError('user should not be undefined here');
    service.getOrInitCurrentUser().subscribe(() => {
      expect(service.getCurrentUser()).toBe(users[0]);
      done();
    });
    const req = httpMock.expectOne('api/v1/users/current');
    req.flush(users[0]);
  });

  it('setIsOkrChampion should call put operation, reloadUsers and reloadCurrentUser', fakeAsync(() => {
    service.setIsOkrChampion(testUser, true).subscribe();
    const req = httpMock.expectOne(`api/v1/users/${testUser.id}/isokrchampion/true`);
    req.flush(users[0]);

    tick();

    const req2 = httpMock.expectOne(`api/v1/users`);
    const req3 = httpMock.expectOne(`api/v1/users/current`);
    req2.flush({});
    req3.flush({});
  }));

  it('createUsers should call createAll and reloadUsers', fakeAsync(() => {
    service.createUsers(users).subscribe();
    const req = httpMock.expectOne(`api/v1/users/createall`);
    req.flush(users);

    tick();

    const req2 = httpMock.expectOne(`api/v1/users`);
    req2.flush({});
  }));

  it('getUserOkrData() should call userokrdata', fakeAsync(() => {
    service.getUserOkrData(testUser).subscribe();
    const req = httpMock.expectOne(`api/v1/users/${testUser.id}/userokrdata`);
    expect(req.request.method).toBe('GET');
    req.flush(testUser);
  }));

  it('deleteUser() should call deleteUser and reloadUsers', fakeAsync(() => {
    service.deleteUser(testUser).subscribe();
    const reqDeleteUser = httpMock.expectOne(`api/v1/users/${testUser.id}`);
    expect(reqDeleteUser.request.method).toBe('DELETE');
    reqDeleteUser.flush(testUser);

    tick();

    const reqReloadUsers = httpMock.expectOne(`api/v1/users`);
    expect(reqReloadUsers.request.method).toBe('GET');
    reqReloadUsers.flush({});
  }));
});
