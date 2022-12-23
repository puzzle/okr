import { TestBed } from '@angular/core/testing';

import { Quarter, QuarterService } from './quarter.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import * as quarterData from '../testing/mock-data/quarters.json';

const response = quarterData.quarters;

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
});
