import { TestBed } from '@angular/core/testing';
import { ErrorInterceptor } from './error-interceptor.service';
import { ToasterService } from '../services/toaster.service';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { ToastrModule } from 'ngx-toastr';
import './../../../global';
import { HttpType } from '../types/enums/HttpType';
import { ToasterType } from '../types/enums/ToasterType';

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

  it.each([
    ['NOT_AUTHORIZED_TO_READ', ['Objective']],
    ['NOT_AUTHORIZED_TO_WRITE', ['Check-in']],
  ])('handleErrorToaster should show correct toaster', (key: string, params: string[]) => {
    const ERROR_PREFIX = 'ERROR.';
    jest.spyOn(translator, 'instant');
    jest.spyOn(toaster, 'showError');
    jest.spyOn(String.prototype, 'format');
    const requestMock = {
      error: {
        errors: [
          {
            errorKey: key,
            params: params,
          },
        ],
      },
    };

    interceptor.handleErrorToaster(requestMock);
    expect(translator.instant).toBeCalledWith(ERROR_PREFIX + key);
    expect(String.prototype.format).toBeCalledWith(params);
    expect(toaster.showError).toBeCalledTimes(1);
  });

  it.each([
    ['/objective/1', 200, HttpType.POST, 'OBJECTIVE', ToasterType.SUCCESS, 'Objective erstellt'],
    ['/keyresult/1', 200, HttpType.PUT, 'KEYRESULT', ToasterType.SUCCESS, 'Keyresult wurde aktualisiert'],
    ['/keyresult/1', 200, HttpType.DELETE, 'KEYRESULT', ToasterType.SUCCESS, 'Keyresult wurde gelöscht'],
  ])(
    'handleSuccessToaster should show toaster ',
    (url: string, code: number, method: HttpType, key: string, toasterType: ToasterType, message: string) => {
      const SUCCESS_PREFIX = 'SUCCESS.';
      jest.spyOn(translator, 'instant').mockReturnValue(message);
      jest.spyOn(toaster, 'showCustomToaster');
      jest.spyOn(interceptor, 'getSuccessMessageKey').mockReturnValue({ key: key, toasterType: toasterType });

      const requestMock = {
        url: url,
        status: code,
      };

      interceptor.handleSuccessToaster(requestMock, method);
      expect(interceptor.getSuccessMessageKey).toBeCalledWith(url, code, method);
      expect(translator.instant).toBeCalledWith(SUCCESS_PREFIX + key);
      expect(toaster.showCustomToaster).toBeCalledWith(message, toasterType);
    },
  );

  it.each([
    ['/objective/1', 200, HttpType.GET, 'OBJECTIVE'],
    ['/keyresult/1', 200, HttpType.GET, 'KEYRESULT'],
    ['/keyresult/1', 200, HttpType.GET, 'KEYRESULT'],
  ])('handleSuccessToaster should not show toaster ', (url: string, code: number, method: HttpType) => {
    jest.spyOn(translator, 'instant');
    jest.spyOn(toaster, 'showCustomToaster');
    jest.spyOn(interceptor, 'getSuccessMessageKey').mockReturnValue(undefined);

    const requestMock = {
      url: url,
      status: code,
    };

    interceptor.handleSuccessToaster(requestMock, method);
    expect(interceptor.getSuccessMessageKey).toBeCalledWith(url, code, method);

    expect(translator.instant).toBeCalledTimes(0);
    expect(toaster.showCustomToaster).toBeCalledTimes(0);
  });

  it.each([
    ['/teams/1', 200, HttpType.GET, undefined],
    ['/teams/1', 200, HttpType.PUT, { key: 'TEAM.PUT', toasterType: ToasterType.SUCCESS }],
    ['/teams/1', 200, HttpType.POST, { key: 'TEAM.POST', toasterType: ToasterType.SUCCESS }],
    ['/teams/1', 200, HttpType.DELETE, { key: 'TEAM.DELETE', toasterType: ToasterType.SUCCESS }],

    ['/objectives/1', 200, HttpType.GET, undefined],
    ['/objectives/1', 200, HttpType.PUT, { key: 'OBJECTIVE.PUT', toasterType: ToasterType.SUCCESS }],
    ['/objectives/1', 200, HttpType.POST, { key: 'OBJECTIVE.POST', toasterType: ToasterType.SUCCESS }],
    ['/objectives/1', 200, HttpType.DELETE, { key: 'OBJECTIVE.DELETE', toasterType: ToasterType.SUCCESS }],
    ['/objectives/1', 226, HttpType.PUT, { key: 'OBJECTIVE.IM_USED', toasterType: ToasterType.WARN }],

    ['/keyresults/1', 200, HttpType.GET, undefined],
    ['/keyresults/1', 200, HttpType.PUT, { key: 'KEY_RESULT.PUT', toasterType: ToasterType.SUCCESS }],
    ['/keyresults/1', 200, HttpType.POST, { key: 'KEY_RESULT.POST', toasterType: ToasterType.SUCCESS }],
    ['/keyresults/1', 200, HttpType.DELETE, { key: 'KEY_RESULT.DELETE', toasterType: ToasterType.SUCCESS }],
    ['/keyresults/1', 226, HttpType.PUT, { key: 'KEY_RESULT.IM_USED', toasterType: ToasterType.WARN }],

    ['/checkIns/1', 200, HttpType.GET, undefined],
    ['/checkIns/1', 200, HttpType.PUT, { key: 'CHECK_IN.PUT', toasterType: ToasterType.SUCCESS }],
    ['/checkIns/1', 200, HttpType.POST, { key: 'CHECK_IN.POST', toasterType: ToasterType.SUCCESS }],
    ['/checkIns/1', 200, HttpType.DELETE, undefined],
  ])('getSuccessMessageKey should work', (url: string, code: number, method: HttpType, result: any) => {
    const successMessageKey = interceptor.getSuccessMessageKey(url, code, method);
    expect(successMessageKey).toStrictEqual(result);
  });

  it.each([
    ['http://localhost:4200/', 'http://localhost:4200/api/objecive/1', true],
    ['http://localhost:4200/', 'http://habasch:4200/api/objecive/1', false],
    ['http://localhost:4200/', 'http://habasch:4200/objecive/1', false],
  ])(
    'checkIfSuccessToasterIsShown should work as intended',
    (currentURL: string, requestURL: string, result: boolean) => {
      const requestMock = { url: requestURL };
      window.location.assign(currentURL);

      const b = interceptor.checkForToaster(requestMock);
      expect(b).toBe(result);
    },
  );
});
