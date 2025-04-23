import { TestBed } from '@angular/core/testing';

import { EvaluationService } from './evaluation.service';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('EvaluationService', () => {
  let service: EvaluationService;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideRouter([]),
      provideHttpClient(),
      provideHttpClientTesting()] });
    service = TestBed.inject(EvaluationService);
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });
});
