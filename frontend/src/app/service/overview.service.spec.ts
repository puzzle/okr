import { TestBed } from '@angular/core/testing';
import { OverviewService } from './overview.service';
import { HttpClient, HttpHandler } from '@angular/common/http';

describe('OverviewService', () => {
  let service: OverviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpClient, HttpHandler],
    });
    service = TestBed.inject(OverviewService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
