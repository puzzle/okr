import { TestBed } from '@angular/core/testing';

import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Overview, OverviewService } from './overview.service';
import * as overviewData from '../testing/mock-data/overview.json';

const response = overviewData.overview;

describe('OverviewService', () => {
  let overviewService: OverviewService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    overviewService = TestBed.inject(OverviewService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  test('should be created', () => {
    expect(overviewService).toBeTruthy();
  });

  test('should get team with their objectives', (done) => {
    overviewService.getOverview(undefined, []).subscribe({
      next(response: Overview[]) {
        expect(response.length).toBe(3);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const request = httpTestingController.expectOne(`api/v1/overview`);
    expect(request.request.method).toEqual('GET');
    request.flush(response);
    httpTestingController.verify();
  });

  test('should set quarter and team filter', (done) => {
    overviewService.getOverview(1, [1, 2]).subscribe({
      next(response: Overview[]) {
        expect(response.length).toBe(3);
        done();
      },
      error(error) {
        done(error);
      },
    });

    const request = httpTestingController.expectOne(`api/v1/overview?quarter=1&team=1,2`);
    expect(request.request.method).toEqual('GET');
    request.flush(response);
    httpTestingController.verify();
  });
});
