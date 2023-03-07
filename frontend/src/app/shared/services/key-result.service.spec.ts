import { TestBed } from '@angular/core/testing';

import { KeyResultMeasure, KeyResultService } from './key-result.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as keyresultData from '../testing/mock-data/keyresults.json';

const response = keyresultData.keyresults;

describe('KeyResultService', () => {
  let service: KeyResultService;
  let httpTestingController: HttpTestingController;
  const URL = '/api/v1/objectives';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(KeyResultService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  test('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('should get Key Results of Objective', (done) => {
    service.getKeyResultsOfObjective(42).subscribe({
      next(response: KeyResultMeasure[]) {
        expect(response.length).toBe(3);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const req = httpTestingController.expectOne(`${URL}/42/keyresults`);
    expect(req.request.method).toEqual('GET');
    req.flush(response);
    httpTestingController.verify();
  });

  test('should throw a error if load failed', (done) => {
    service.getKeyResultsOfObjective(42).subscribe({
      next() {
        done('should have a 404 Not Found');
      },
      error() {
        done();
      },
    });
    const req = httpTestingController.expectOne(`${URL}/42/keyresults`);
    expect(req.request.method).toEqual('GET');
    req.flush(null, { status: 404, statusText: 'Not Found' });
    httpTestingController.verify();
  });
});
