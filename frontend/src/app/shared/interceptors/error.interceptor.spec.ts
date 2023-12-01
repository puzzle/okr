import { TestBed } from '@angular/core/testing';
import { ErrorInterceptor } from './error-interceptor.service';
import { ToasterService } from '../services/toaster.service';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { ToastrModule } from 'ngx-toastr';
import './../../../global';

describe('ErrorInterceptor', () => {
  let interceptor: ErrorInterceptor;
  let router: Router;
  let translator: TranslateService;
  let toaster: ToasterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ToastrModule.forRoot(), TranslateModule.forRoot()],
      providers: [ErrorInterceptor, ToasterService, TranslateService],
    });
    interceptor = TestBed.inject(ErrorInterceptor);
    router = TestBed.inject(Router);
    translator = TestBed.inject(TranslateService);
    toaster = TestBed.inject(ToasterService);
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

  it('handleErrorToaster should show correct errors', () => {
    jest.spyOn(translator, 'instant');
    jest.spyOn(toaster, 'showError');
    jest.spyOn(String.prototype, 'format');
    const requestMock = {
      error: {
        errors: [
          {
            errorKey: 'NOT_AUTHORIZED_TO_READ',
            params: ['Objective'],
          },
        ],
      },
    };

    interceptor.handleErrorToaster(requestMock);
    expect(translator.instant).toBeCalledWith('ERROR.NOT_AUTHORIZED_TO_READ');
    expect(String.prototype.format).toBeCalledWith(['Objective']);
    expect(toaster.showError).toBeCalledTimes(1);
  });
});
