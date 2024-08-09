import { TestBed } from '@angular/core/testing';

import { QuarterService } from './quarter.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('QuarterService', () => {
  let service: QuarterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(QuarterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
