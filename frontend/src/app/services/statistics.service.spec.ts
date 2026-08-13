import { fakeAsync, TestBed, tick } from '@angular/core/testing';

import { StatisticsService } from './statistics.service';
import { EvaluationService } from './evaluation.service';

import { statistics } from '../shared/test-data';
import { of } from 'rxjs';

describe('StatisticsService', () => {
  let service: StatisticsService;
  let evaluationServiceMock: { getStatistics: jest.Mock };

  const requestParams = { quarter: '7',
    teams: '5' };
  const expectedParams = { quarter: 7,
    teams: [5] };

  beforeEach(() => {
    evaluationServiceMock = {
      getStatistics: jest.fn()
    };

    TestBed.configureTestingModule({
      providers: [StatisticsService,
        { provide: EvaluationService,
          useValue: evaluationServiceMock }]
    });

    service = TestBed.inject(StatisticsService);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  it('should be initialised with null filter and data', () => {
    expect(service.publicFilter())
      .toBeNull();
    expect(service.data())
      .toBeUndefined();
    expect(evaluationServiceMock.getStatistics).not.toHaveBeenCalled();
  });

  it('should load statistics on load', fakeAsync(() => {
    evaluationServiceMock.getStatistics.mockReturnValue(of(statistics));

    service.load(requestParams);

    assertThatValuesLoaded(expectedParams);
  }));

  it('should load statistics on reload', fakeAsync(() => {
    evaluationServiceMock.getStatistics.mockReturnValue(of(statistics));

    service.load(requestParams);
    TestBed.tick();
    tick();

    jest.clearAllMocks(); // reset the call history

    service.reload();

    assertThatValuesLoaded(expectedParams);
  }));

  function assertThatValuesLoaded(expectedParams: { quarter: number;
    teams: number[]; }) {
    TestBed.tick();
    tick();

    expect(service.publicFilter()?.quarterId)
      .toBe(expectedParams.quarter);
    expect(service.publicFilter()?.teamIds)
      .toStrictEqual(expectedParams.teams);

    expect(evaluationServiceMock.getStatistics)
      .toHaveBeenCalledWith(expectedParams.quarter, expectedParams.teams);

    expect(service.data())
      .toBe(statistics);
  }
});
