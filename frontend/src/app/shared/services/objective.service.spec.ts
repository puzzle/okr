import { TestBed } from '@angular/core/testing';

import { ObjectiveService } from './objective.service';

describe('ObjectiveService', () => {
  let service: ObjectiveService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ObjectiveService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
