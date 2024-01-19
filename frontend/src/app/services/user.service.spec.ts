import { getTestBed, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UserService } from './user.service';
import { users } from '../shared/testData';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;
  const URL = 'api/v1/users';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(UserService);
    const injector = getTestBed();
    httpMock = injector.get(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  test('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('getUsers should only reload users when they are not loaded yet', (done) => {
    const spy = jest.spyOn(service, 'reloadUsers');
    service.getUsers().subscribe(() => {
      expect(spy).toBeCalledTimes(1);
      httpMock.expectOne(URL);
      service.getUsers().subscribe((users) => {
        expect(spy).toBeCalledTimes(1);
        expect(users).toStrictEqual([]);
        done();
      });
    });
  });

  test('get current user should throw error, when not loaded', () => {
    expect(() => service.getCurrentUser()).toThrowError('user should not be undefined here');
  });

  test('init current user should load user', (done) => {
    expect(() => service.getCurrentUser()).toThrowError('user should not be undefined here');
    service.getOrInitCurrentUser().subscribe(() => {
      expect(service.getCurrentUser()).toBe(users[0]);
      done();
    });
    const req = httpMock.expectOne('api/v1/users/current');
    req.flush(users[0]);
  });
});
