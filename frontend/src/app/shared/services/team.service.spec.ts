import { TestBed } from '@angular/core/testing';
import { Team, TeamService } from './team.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

const respons = [
  {
    id: 1,
    name: 'Team 1',
  },
];

describe('TeamService', () => {
  let service: TeamService;
  let httpTestingController: HttpTestingController;
  const URL = 'api/v1/teams';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(TeamService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  test('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('should return 6 quarters', () => {
    let currentYear = new Date().getFullYear();
    let currentQuarter: number = Math.floor(new Date().getMonth() / 3 + 1);
    let quarterList = service.getQuarter();
    expect(quarterList.length).toEqual(6);
    expect(quarterList[0].quarter).toEqual(
      currentYear.toString().slice(-2) + '-' + currentQuarter
    );
    if (currentQuarter == 4) {
      expect(quarterList[5].quarter).toEqual(
        (currentYear + 1).toString().slice(-2) + '-' + 1
      );
    } else {
      expect(quarterList[5].quarter).toEqual(
        currentYear.toString().slice(-2) + '-' + (currentQuarter + 1)
      );
    }
  });

  test('should return first quarter at last march', () => {
    let firstQuarter = new Date('2022-03-31T00:00:00');
    let quarterList = service.getQuarter(firstQuarter);
    expect(quarterList.length).toEqual(6);
    expect(quarterList[0].quarter).toEqual('22-1');
    expect(quarterList[1].quarter).toEqual('21-4');
    expect(quarterList[5].quarter).toEqual('22-2');
  });

  test('should return second quarter at first april', () => {
    let secondQuarter = new Date('2022-04-01T00:00:00');
    let quarterList = service.getQuarter(secondQuarter);
    expect(quarterList.length).toEqual(6);
    expect(quarterList[0].quarter).toEqual('22-2');
    expect(quarterList[1].quarter).toEqual('22-1');
    expect(quarterList[5].quarter).toEqual('22-3');
  });

  test('should return right quarters at beginning of year', () => {
    let beginningOfYear = new Date('2022-01-01T00:00:00');
    let quarterList = service.getQuarter(beginningOfYear);
    expect(quarterList.length).toEqual(6);
    expect(quarterList[0].quarter).toEqual('22-1');
    expect(quarterList[1].quarter).toEqual('21-4');
    expect(quarterList[2].quarter).toEqual('21-3');
    expect(quarterList[3].quarter).toEqual('21-2');
    expect(quarterList[4].quarter).toEqual('21-1');
    expect(quarterList[5].quarter).toEqual('22-2');
  });

  test('should return right quarters in the middle of year', () => {
    let middleOfYear = new Date('2018-06-15T00:00:00');
    let quarterList = service.getQuarter(middleOfYear);
    expect(quarterList.length).toEqual(6);
    expect(quarterList[0].quarter).toEqual('18-2');
    expect(quarterList[1].quarter).toEqual('18-1');
    expect(quarterList[2].quarter).toEqual('17-4');
    expect(quarterList[3].quarter).toEqual('17-3');
    expect(quarterList[4].quarter).toEqual('17-2');
    expect(quarterList[5].quarter).toEqual('18-3');
  });

  test('should return right quarters at end of year', () => {
    let endOfYear = new Date('2010-12-31T00:00:00');
    let quarterList = service.getQuarter(endOfYear);
    expect(quarterList.length).toEqual(6);
    expect(quarterList[0].quarter).toEqual('10-4');
    expect(quarterList[1].quarter).toEqual('10-3');
    expect(quarterList[2].quarter).toEqual('10-2');
    expect(quarterList[3].quarter).toEqual('10-1');
    expect(quarterList[4].quarter).toEqual('09-4');
    expect(quarterList[5].quarter).toEqual('11-1');
  });

  test('should get Teams', (done) => {
    service.getTeams().subscribe({
      next(response: Team[]) {
        expect(response.length).toBe(1);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const req = httpTestingController.expectOne(`${URL}`);
    expect(req.request.method).toEqual('GET');
    req.flush(respons);
    httpTestingController.verify();
  });
});
