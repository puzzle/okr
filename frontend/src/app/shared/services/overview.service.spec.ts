import { TestBed } from '@angular/core/testing';

import { OverviewService } from './overview.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { State } from '../types/enums/State';
import { overViewEntity1, quarter } from '../testData';

const httpClient = {
  get: jest.fn(),
};

describe('OverviewService', () => {
  let service: OverviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{ provide: HttpClient, useValue: httpClient }],
    }).compileComponents();
    service = TestBed.inject(OverviewService);
    httpClient.get.mockReturnValue(
      of([{ ...overViewEntity1, objectives: { ...overViewEntity1.objectives[0], state: 'DRAFT' } }]),
    );
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should map correctly', () => {
    service.getOverview().subscribe((overviews) => {
      expect(overviews[0].objectives[0].state).toBe(State.DRAFT);
    });
  });

  it('should make call to overview endpoint when quarterId is undefined', () => {
    service.getOverview();
    expect(httpClient.get).toHaveBeenCalledWith('/api/v2/overview', { params: {} });
  });

  it('should make call to overview endpoint with overview url param when quarterId is defined', () => {
    service.getOverview(quarter.id);
    expect(httpClient.get).toHaveBeenCalledWith('/api/v2/overview', { params: { quarter: 1 } });
  });
});
