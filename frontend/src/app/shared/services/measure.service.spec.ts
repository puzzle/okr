import { TestBed } from '@angular/core/testing';

import { MeasureService } from './measure.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('MeasureService', () => {
  let service: MeasureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(MeasureService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
