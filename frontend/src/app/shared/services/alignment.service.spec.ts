import { TestBed } from '@angular/core/testing';

import { AlignmentService } from './alignment.service';

describe('AlignmentService', () => {
  let service: AlignmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AlignmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
