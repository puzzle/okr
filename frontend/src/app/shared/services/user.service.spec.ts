import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { User, UserService } from './user.service';
import * as userData from '../testing/mock-data/users.json';

const response = userData.users;
describe('UserService', () => {
  let service: UserService;
  let httpTestingController: HttpTestingController;
  const URL = 'api/v1/users';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  test('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('should get Users', (done) => {
    service.getUsers().subscribe({
      next(response: User[]) {
        expect(response.length).toBe(3);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const req = httpTestingController.expectOne(`${URL}`);
    expect(req.request.method).toEqual('GET');
    req.flush(response);
    httpTestingController.verify();
  });
});
