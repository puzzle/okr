import { TestBed } from '@angular/core/testing';

import { Goal, GoalService } from './goal.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import * as goalsData from '../testing/mock-data/goals.json';

const response = goalsData.goals[0];

describe('GoalService', () => {
  let service: GoalService;
  let httpTestingController: HttpTestingController;
  const URL = '/api/v1/goals';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(GoalService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  test('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('should get goal by Key Result id', (done) => {
    service.getGoalByKeyResultId(42).subscribe({
      next(response: Goal) {
        expect(response).toEqual(response);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const req = httpTestingController.expectOne(`${URL}/42`);
    expect(req.request.method).toEqual('GET');
    req.flush(response);
    httpTestingController.verify();
  });
});
