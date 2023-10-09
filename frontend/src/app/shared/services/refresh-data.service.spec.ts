import { TestBed } from '@angular/core/testing';

import { RefreshDataService } from './refresh-data.service';

describe('RefreshDataService', () => {
  let service: RefreshDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RefreshDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
