import { TestBed } from '@angular/core/testing';

import { KeyresultService } from './keyresult.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('KeyresultService', () => {
  let service: KeyresultService;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [HttpClientTestingModule] });
    service = TestBed.inject(KeyresultService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
