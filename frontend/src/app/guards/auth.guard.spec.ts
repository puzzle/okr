import { TestBed } from '@angular/core/testing';
import { CanActivateFn, Router } from '@angular/router';

import { authGuard } from './auth.guard';
import { OAuthService } from 'angular-oauth2-oidc';
import { of } from 'rxjs';

const oAuthMock = {
  initCodeFlow: jest.fn(),
  loadDiscoveryDocumentAndTryLogin: jest.fn(),
  hasValidIdToken: jest.fn(),
  setupAutomaticSilentRefresh: jest.fn(),
};

const routerMock = {
  navigateByUrl: jest.fn(),
};

const route = { queryParamMap: new Map() };

describe('authGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) =>
    TestBed.runInInjectionContext(() => authGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: OAuthService,
          useValue: oAuthMock,
        },
        {
          provide: Router,
          useValue: routerMock,
        },
      ],
    });
    jest.spyOn(oAuthMock, 'initCodeFlow').mockReturnValue(true);
    jest.spyOn(oAuthMock, 'loadDiscoveryDocumentAndTryLogin').mockReturnValue(Promise.resolve(true));
    jest.spyOn(oAuthMock, 'setupAutomaticSilentRefresh').mockReturnValue(true);
    jest.spyOn(routerMock, 'navigateByUrl').mockReturnValue(of().toPromise());
    oAuthMock.initCodeFlow.mockReset();
    oAuthMock.setupAutomaticSilentRefresh.mockReset();
    routerMock.navigateByUrl.mockReset();
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should not call initCodeFlow if token is valid and call router if state param exist', async () => {
    jest.spyOn(oAuthMock, 'hasValidIdToken').mockReturnValue(true);
    route.queryParamMap.set('state', 1234);

    const result = await runAuthGuardWithContext(authGuard);

    expect(result).toBe(false);
    expect(oAuthMock.loadDiscoveryDocumentAndTryLogin).toHaveBeenCalled();
    expect(oAuthMock.initCodeFlow).not.toHaveBeenCalled();
    expect(oAuthMock.setupAutomaticSilentRefresh).toHaveBeenCalled();

    expect(routerMock.navigateByUrl).toHaveBeenCalled();
  });

  it('should not call router if state param does not exist', async () => {
    jest.spyOn(oAuthMock, 'hasValidIdToken').mockReturnValue(true);
    route.queryParamMap.set('state', null);

    const result = await runAuthGuardWithContext(authGuard);

    expect(result).toBeTruthy();
    expect(routerMock.navigateByUrl).not.toHaveBeenCalled();
  });

  it('should call initCodeFlow if token is invalid', async () => {
    jest.spyOn(oAuthMock, 'hasValidIdToken').mockReturnValue(false);
    const result = await runAuthGuardWithContext(authGuard);

    expect(result).toBe(false);
    expect(oAuthMock.loadDiscoveryDocumentAndTryLogin).toHaveBeenCalled();
    expect(oAuthMock.initCodeFlow).toHaveBeenCalled();
    expect(oAuthMock.setupAutomaticSilentRefresh).not.toHaveBeenCalled();
  });

  async function runAuthGuardWithContext(authGuard: any): Promise<boolean> {
    return await TestBed.runInInjectionContext(() => authGuard(route, null));
  }
});
