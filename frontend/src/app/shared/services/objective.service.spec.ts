import { TestBed } from '@angular/core/testing';

import { Objective, ObjectiveService } from './objective.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import * as objectivesData from '../testing/mock-data/objectives.json';

const response = objectivesData.objectives;

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
        expect(response.length).toBe(3);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const req = httpTestingController.expectOne(`${URL}/42/objectives`);
    expect(req.request.method).toEqual('GET');
    req.flush(response);
    httpTestingController.verify();
  });
});
