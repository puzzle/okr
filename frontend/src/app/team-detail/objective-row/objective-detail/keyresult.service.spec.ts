import { TestBed } from '@angular/core/testing';

import { KeyresultService } from './keyresult.service';

describe('KeyresultService', () => {
  let service: KeyresultService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(KeyresultService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
