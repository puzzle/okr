import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot } from '@angular/router';

import { authGuard } from './auth.guard';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { of } from 'rxjs';

const oAuthMock = {
  get isAuthenticated$() {
    return of({});
  },
};

describe('authGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) =>
    TestBed.runInInjectionContext(() => authGuard(...guardParameters));
  const route: ActivatedRouteSnapshot = {} as any;
  const state: RouterStateSnapshot = {} as any;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: OidcSecurityService,
          useValue: oAuthMock,
        },
      ],
    });
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should return true if token valid ', async () => {
    jest.spyOn(oAuthMock, 'isAuthenticated$', 'get').mockReturnValue(of({ isAuthenticated: true }));
    const result$ = runAuthGuardWithContext(authGuard, route, state);

    result$.then((result) => {
      expect(result).toBe(true);
    });
  });

  it('should return false if token invalid', async () => {
    jest.spyOn(oAuthMock, 'isAuthenticated$', 'get').mockReturnValue(of({ isAuthenticated: false }));
    const result$ = runAuthGuardWithContext(authGuard, route, state);

    result$.then((result) => {
      expect(result).toBe(false);
    });
  });

  async function runAuthGuardWithContext(authGuard: any, route: any, state: any): Promise<boolean> {
    return await TestBed.runInInjectionContext(() => authGuard(route, state));
  }
});
