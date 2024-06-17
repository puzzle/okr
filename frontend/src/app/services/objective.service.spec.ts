import { TestBed } from '@angular/core/testing';

import { ObjectiveService } from './objective.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ObjectiveService', () => {
  let service: ObjectiveService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(ObjectiveService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
