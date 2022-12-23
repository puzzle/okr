import { TestBed } from '@angular/core/testing';
import { Team, TeamService } from './team.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import * as teamData from '../testing/mock-data/teams.json';

const teamResponse: Team = teamData.teams[0];

const teamListResponse = [teamResponse];

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

  test('should get Team', (done) => {
    let id: number = 1;
    service.getTeam(id).subscribe({
      next(response: Team) {
        expect(response.name).toBe('Team 1');
        done();
      },
      error(error) {
        done(error);
      },
    });

    const req = httpTestingController.expectOne(`${URL}/` + id);
    expect(req.request.method).toEqual('GET');
    req.flush(teamResponse);
    httpTestingController.verify();
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
    req.flush(teamListResponse);
    httpTestingController.verify();
  });

  test('should create Team', (done) => {
    let team: Team = {
      id: undefined,
      name: 'Team 22',
    };
    service.save(team).subscribe({
      next(response: Team) {
        expect(response).toBe(teamListResponse);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const req = httpTestingController.expectOne(`${URL}`);
    expect(req.request.method).toEqual('POST');
    req.flush(teamListResponse);
    expect(req.request.body).toEqual({ id: undefined, name: 'Team 22' });
    httpTestingController.verify();
  });

  test('should update Team', (done) => {
    let team: Team = {
      id: 22,
      name: 'Team 22',
    };
    service.save(team).subscribe({
      next(response: Team) {
        expect(response).toBe(teamListResponse);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const req = httpTestingController.expectOne(`${URL}/22`);
    expect(req.request.method).toEqual('PUT');
    req.flush(teamListResponse);
    expect(req.request.body).toEqual({ id: 22, name: 'Team 22' });
    httpTestingController.verify();
  });
});
