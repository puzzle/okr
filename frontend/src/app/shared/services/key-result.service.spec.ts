import { TestBed } from '@angular/core/testing';

import { KeyResultService } from './key-result.service';

describe('KeyResultService', () => {
  let service: KeyResultService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(KeyResultService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
