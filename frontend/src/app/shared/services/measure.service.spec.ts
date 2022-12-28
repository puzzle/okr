import { TestBed } from '@angular/core/testing';

import { Measure, MeasureService } from './measure.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import * as measureData from '../testing/mock-data/measure.json';

const respons = measureData.measure;
const badRespons = 'Did not find the Measure with requested id';

describe('MeasureService', () => {
  let service: MeasureService;
  let httpTestingController: HttpTestingController;
  const URL = '/api/v1/measures';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(MeasureService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('should get measure by id', (done) => {
    service.getMeasureById(1).subscribe({
      next(response: Measure) {
        expect(response).toEqual(respons);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const req = httpTestingController.expectOne(`${URL}/1`);
    expect(req.request.method).toEqual('GET');
    req.flush(respons);
    httpTestingController.verify();
  });

  test('should return error when no measure with id', (done) => {
    service.getMeasureById(34567).subscribe({
      next() {
        done('should have a 404 Not Found');
      },
      error() {
        done();
      },
    });

    const req = httpTestingController.expectOne(`${URL}/34567`);
    expect(req.request.method).toEqual('GET');
    req.flush(null, { status: 404, statusText: 'Not Found' });
    httpTestingController.verify();
  });
});
