import { TestBed } from '@angular/core/testing';
import { ErrorInterceptor } from './error-interceptor.service';
import { ToasterService } from '../services/toaster.service';
import { TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';

describe('ErrorInterceptor', () => {
  let interceptor: ErrorInterceptor;
  let router: Router;

  beforeEach(() => {
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
    });
    interceptor = TestBed.inject(ErrorInterceptor);
    router = TestBed.inject(Router);
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });

  it.each([
    ['test', 0],
    ['objective', 1],
    ['keyresult', 1],
  ])('handleDrawerError on route %p should be called %p times', (url: string, isCalledTimes: number) => {
    const requestMock = { url: url };
    jest.spyOn(router, 'navigate');

    interceptor.handleDrawerError(requestMock);

    expect(router.navigate).toHaveBeenCalledTimes(isCalledTimes);
  });
});
