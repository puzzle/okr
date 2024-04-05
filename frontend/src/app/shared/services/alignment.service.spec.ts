import { TestBed } from '@angular/core/testing';

import { AlignmentService } from './alignment.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpClient } from '@angular/common/http';

const httpClient = {
  get: jest.fn(),
};

describe('AlignmentService', () => {
  let service: AlignmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{ provide: HttpClient, useValue: httpClient }],
    }).compileComponents();
    service = TestBed.inject(AlignmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
