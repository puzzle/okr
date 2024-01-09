import { getTestBed, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UserService } from './user.service';
import { users } from '../shared/testData';

const response = users;

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

  test('getUsers should only reload users when they are not loaded yet', () => {
    service.getUsers().subscribe((users) => {
      expect(service.reloadUsers).toBeCalledTimes(1);
      service.getUsers().subscribe(() => {
        expect(service.reloadUsers).toBeCalledTimes(0);
        expect(service.getUsers()).toBe([{ test }]);
      });
    });
  });

  test('get current user should throw error, when not loaded', () => {
    expect(() => service.getCurrentUser()).toThrowError('user should not be undefined here');
  });

  test('init current user should load user', () => {
    expect(() => service.getCurrentUser()).toThrowError('user should not be undefined here');
    service.initCurrentUser().subscribe(() => {
      expect(service.getCurrentUser()).toBe({ test });
    });
  });
});
