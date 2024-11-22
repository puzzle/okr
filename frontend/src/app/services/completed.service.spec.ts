import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CompletedService } from './completed.servce';

describe('CompletedService', () => {
  let service: CompletedService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(CompletedService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
