import { TestBed } from '@angular/core/testing';
import { ErrorInterceptor } from './error-interceptor.service';
import { ToasterService } from '../services/toaster.service';
import { TranslateService } from '@ngx-translate/core';

describe('ErrorInterceptor', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      providers: [
        ErrorInterceptor,
        {
          provide: ToasterService,
          useValue: {},
        },
        {
          provide: TranslateService,
          useValue: {},
        },
      ],
    }),
  );

  it('should be created', () => {
    const interceptor: ErrorInterceptor = TestBed.inject(ErrorInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
