import { TestBed } from '@angular/core/testing';

import { Objective, ObjectiveService } from './objective.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

const respons = [
  {
    id: 1,
    title: 'Objective 1',
    ownerId: 1,
    ownerFirstname: 'Alice',
    ownerLastname: 'Wunderland',
    teamId: 1,
    teamName: 'Team 1',
    quarterId: 1,
    quarterLabel: 'GJ 22/23-Q1',
    description: 'This is the description of Objective 1',
    progress: 20,
  },
];

describe('ObjectiveService', () => {
  let service: ObjectiveService;
  let httpTestingController: HttpTestingController;
  const URL = 'api/v1/teams';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(ObjectiveService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  test('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('should get Objectives', (done) => {
    service.getObjectivesOfTeam(42).subscribe({
      next(response: Objective[]) {
        expect(response.length).toBe(1);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const req = httpTestingController.expectOne(`${URL}/42/objectives`);
    expect(req.request.method).toEqual('GET');
    req.flush(respons);
    httpTestingController.verify();
  });
});
