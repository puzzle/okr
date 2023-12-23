import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { authGuard } from './auth.guard';
import { OAuthService } from 'angular-oauth2-oidc';

const oAuthMock = {
  initCodeFlow: jest.fn(),
  loadDiscoveryDocumentAndTryLogin: jest.fn(),
  hasValidIdToken: jest.fn(),
  setupAutomaticSilentRefresh: jest.fn(),
};

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
      ],
    });
    jest.spyOn(oAuthMock, 'initCodeFlow').mockReturnValue(true);
    jest.spyOn(oAuthMock, 'loadDiscoveryDocumentAndTryLogin').mockReturnValue(Promise.resolve(true));
    jest.spyOn(oAuthMock, 'setupAutomaticSilentRefresh').mockReturnValue(true);
    oAuthMock.initCodeFlow.mockReset();
    oAuthMock.setupAutomaticSilentRefresh.mockReset();
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should not call initCodeFlow if token is valid', async () => {
    jest.spyOn(oAuthMock, 'hasValidIdToken').mockReturnValue(true);
    const result = await runAuthGuardWithContext(authGuard);

    expect(result).toBe(true);
    expect(oAuthMock.loadDiscoveryDocumentAndTryLogin).toHaveBeenCalled();
    expect(oAuthMock.initCodeFlow).not.toHaveBeenCalled();
    expect(oAuthMock.setupAutomaticSilentRefresh).toHaveBeenCalled();
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
    return await TestBed.runInInjectionContext(authGuard);
  }
});
