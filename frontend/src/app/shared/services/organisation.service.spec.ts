import { TestBed } from '@angular/core/testing';

import { OrganisationService } from './organisation.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('OrganisationService', () => {
  let service: OrganisationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(OrganisationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
