import { TestBed } from '@angular/core/testing';

import { KeyResultService } from './key-result.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('KeyResultService', () => {
  let service: KeyResultService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(KeyResultService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
