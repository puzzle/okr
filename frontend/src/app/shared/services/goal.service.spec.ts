import { TestBed } from '@angular/core/testing';

import { Goal, GoalService } from './goal.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

const respons = [
  {
    objective: {
      id: 1,
      title: 'Objective title',
      description: 'Objective description',
    },
    keyresult: {
      id: 1,
      title: 'KeyResult title',
      description: 'KeyResult description',
    },
    teamId: 1,
    teamName: 'Team 1',
    progress: 30,
    quarterLabel: 'GJ 22/23-Q1',
    expectedEvolution: 'INCREASE',
    unit: 'PERCENT',
    basicValue: 1,
    targetValue: 100,
  },
];

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

  test('should get goal by keyresult id', (done) => {
    service.getGoalByKeyResultId(42).subscribe({
      next(response: Goal) {
        expect(response).toEqual(respons);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const req = httpTestingController.expectOne(`${URL}/42`);
    expect(req.request.method).toEqual('GET');
    req.flush(respons);
    httpTestingController.verify();
  });
});
