import { TestBed } from '@angular/core/testing';

import { SvgService } from './svg.service';

describe('ProgressBarService', () => {
  let service: SvgService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SvgService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
