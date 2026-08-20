import { fakeAsync, TestBed, tick } from '@angular/core/testing';

import { StatisticsService } from './statistics.service';
import { EvaluationService } from './evaluation.service';

import { statistics } from '../shared/test-data';
import { of } from 'rxjs';

describe('StatisticsService', () => {
  let service: StatisticsService;
  const evaluationServiceMock = {
    getStatistics: jest.fn()
  };

  const requestParams = { quarter: '7',
    teams: '5' };
  const expectedParams = { quarterId: 7,
    teamIds: [5] };

  beforeEach(() => {
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

    TestBed.tick();
    tick();

    expect(service.publicFilter())
      .toStrictEqual(expect.objectContaining(expectedParams));

    expect(evaluationServiceMock.getStatistics)
      .toHaveBeenCalledWith(expectedParams.quarterId, expectedParams.teamIds);

    expect(service.data())
      .toBe(statistics);
  }));

  it('should load statistics on reload', fakeAsync(() => {
    const reloadSpy = jest.spyOn(service.statisticsResource, 'reload');

    service.reload();

    expect(reloadSpy)
      .toHaveBeenCalledTimes(1);
  }));
});
