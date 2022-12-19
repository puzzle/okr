import { TestBed } from '@angular/core/testing';

import { QuarterService } from './quarter.service';

describe('QuarterService', () => {
  let service: QuarterService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuarterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
