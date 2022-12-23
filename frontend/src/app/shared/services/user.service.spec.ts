import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { User, UserService } from './user.service';

const response = [
  {
    id: 1,
    username: 'alicewunderland',
    firstname: 'Alice',
    lastname: 'Wunderland',
    email: 'alice@wunderland.ch',
  },
  {
    id: 2,
    username: 'pacoegiman',
    firstname: 'Paco',
    lastname: 'Egiman',
    email: 'paco@egiman.ch',
  },
];
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
        expect(response.length).toBe(2);
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
