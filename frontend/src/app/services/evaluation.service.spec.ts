import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { HttpParams, provideHttpClient } from '@angular/common/http';

import { EvaluationService } from './evaluation.service';

describe('EvaluationService', () => {
  let service: EvaluationService;
  let httpMock: HttpTestingController;
  const API_URL = '/api/v2/evaluation';

  // Define placeholder mock statistics data
  const mockStatistics: any = { successRate: 100,
    totalEvaluations: 10 };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        provideHttpClient(), // Provide the standard HttpClient
        provideHttpClientTesting(), // Provide the testing backend and controller
        EvaluationService // Optional: Explicitly provide the service under test
      ]
      // No imports array needed for HttpClientTestingModule
    });
    service = TestBed.inject(EvaluationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Verify that no requests are outstanding
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  describe('getStatistics', () => {
    // Define test cases using Jest's each
    it.each([
      {
        description: 'valid quarterId and teamIds',
        quarterId: 1,
        teamIds: [10,
          20],
        expectedParams: new HttpParams()
          .set('quarter', '1')
          .append('team', '10')
          .append('team', '20')
      },
      {
        description: 'valid quarterId and empty teamIds array',
        quarterId: 3,
        teamIds: [],
        expectedParams: new HttpParams()
          .set('quarter', '3') // team key should be absent
      },
      {
        description: 'valid quarterId and null teamIds (as any)',
        quarterId: 4,
        teamIds: null as any,
        expectedParams: new HttpParams()
          .set('quarter', '4') // null/undefined values are omitted by HttpParams
      },
      {
        description: 'valid quarterId and undefined teamIds (as any)',
        quarterId: 5,
        teamIds: undefined as any,
        expectedParams: new HttpParams()
          .set('quarter', '5') // null/undefined values are omitted by HttpParams
      },
      {
        description: 'null quarterId (as any) and valid teamIds',
        quarterId: null as any,
        teamIds: [7],
        expectedParams: new HttpParams()
          .append('team', '7') // null/undefined values are omitted by HttpParams
      },
      {
        description: 'undefined quarterId (as any) and valid teamIds',
        quarterId: undefined as any,
        teamIds: [8],
        expectedParams: new HttpParams()
          .append('team', '8') // null/undefined values are omitted by HttpParams
      },
      {
        description: 'undefined quarterId and undefined teamIds (as any)',
        quarterId: undefined as any,
        teamIds: undefined as any,
        expectedParams: new HttpParams() // No parameters expected
      },
      {
        description: 'null quarterId and null teamIds (as any)',
        quarterId: null as any,
        teamIds: null as any,
        expectedParams: new HttpParams() // No parameters expected
      }
    ])('should call http.get with correct URL and parameters for case: $description', ({ quarterId, teamIds, expectedParams }) => {
      service.getStatistics(quarterId, teamIds)
        .subscribe(); // Subscribe to trigger the call

      const req = httpMock.expectOne((request) => request.url === API_URL &&
        request.method === 'GET');

      // Compare HttpParams objects - comparing their string representation is reliable
      expect(req.request.params.toString())
        .toEqual(expectedParams.toString());

      req.flush(mockStatistics); // Flush with mock data to complete the request
    });
  });
});

/*
 * Placeholder for the Statistics type if not imported
 * You should replace this with your actual import or definition
 * interface Statistics {
 *   successRate: number;
 *   totalEvaluations: number;
 *   // Define the structure of your Statistics object
 * }
 */
