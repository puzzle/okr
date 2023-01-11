import { TestBed } from '@angular/core/testing';

import { Measure, MeasureService } from './measure.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { loadMeasure } from '../testing/Loader';

const respons = loadMeasure('measure');

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

  test('should create measure', (done) => {
    let measure: Measure = {
      id: null,
      measureDate: new Date('2022-12-24'),
      createdOn: new Date('2023-01-18'),
      createdById: 0,
      changeInfo: 'Changeinfo',
      initiatives: 'Initiatives',
      value: 42,
      keyResultId: 1,
    };
    service.saveMeasure(measure, true).subscribe({
      next(response: Measure) {
        expect(response).toBe(respons);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const req = httpTestingController.expectOne(`${URL}`);
    expect(req.request.method).toEqual('POST');
    req.flush(respons);
    expect(req.request.body.changeInfo).toContain('Changeinfo');
    expect(req.request.body.value).toEqual(42);
    expect(req.request.body.measureDate).toEqual(new Date('2022-12-24'));
    httpTestingController.verify();
  });

  test('should update Measure', (done) => {
    let measure: Measure = {
      id: 33,
      measureDate: new Date('2022-12-24'),
      createdOn: new Date('2022-12-28'),
      createdById: 0,
      changeInfo: 'Changeinfo',
      initiatives: 'Initiatives',
      value: 42,
      keyResultId: 1,
    };
    service.saveMeasure(measure, false).subscribe({
      next(response: Measure) {
        expect(response).toBe(respons);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const req = httpTestingController.expectOne(`${URL}/33`);
    expect(req.request.method).toEqual('PUT');
    req.flush(respons);
    expect(req.request.body).toEqual({
      id: 33,
      measureDate: new Date('2022-12-24'),
      createdOn: new Date('2022-12-28'),
      createdById: 0,
      changeInfo: 'Changeinfo',
      initiatives: 'Initiatives',
      value: 42,
      keyResultId: 1,
    });
    httpTestingController.verify();
  });
});
