import { TestBed } from '@angular/core/testing';

import { TeamService } from './team.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('TeamService', () => {
  let service: TeamService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(TeamService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
