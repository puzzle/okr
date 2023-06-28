import { TestBed } from '@angular/core/testing';

import { Quarter, QuarterService, StartEndDateDTO } from './quarter.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as quarterData from '../testing/mock-data/quarters.json';

describe('QuarterService', () => {
  let quarterService: QuarterService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    quarterService = TestBed.inject(QuarterService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  test('should be created', () => {
    expect(quarterService).toBeTruthy();
  });

  test('should get Quarters', (done) => {
    const response = quarterData.quarters;
    quarterService.getQuarters().subscribe({
      next(response: Quarter[]) {
        expect(response.length).toBe(5);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const request = httpTestingController.expectOne(`api/v1/quarters`);
    expect(request.request.method).toEqual('GET');
    request.flush(response);
    httpTestingController.verify();
  });

  test('should get start and end date of keyresult', (done) => {
    let id = 1;
    const response = quarterData.startAndEndDates[0];
    quarterService.getStartAndEndDateOfKeyresult(id).subscribe({
      next(response: StartEndDateDTO) {
        expect(response.startDate).toEqual('2023-01-1T00:00:00.000Z');
        expect(response.endDate).toEqual('2023-03-31T023:59:59.999Z');
        done();
      },
      error(error) {
        done(error);
      },
    });

    const request = httpTestingController.expectOne(`api/v1/quarters/dates/` + id);
    expect(request.request.method).toEqual('GET');
    request.flush(response);
    httpTestingController.verify();
  });
});
