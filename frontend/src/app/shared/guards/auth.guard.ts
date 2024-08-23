import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';

export const authGuard: CanActivateFn = (route, state) => {
  const oauthService = inject(OAuthService);
  const router = inject(Router);

  console.log('test123');
  return oauthService.loadDiscoveryDocumentAndTryLogin().then(async () => {
    // if the login failed initialize code flow
    let validToken = oauthService.hasValidIdToken();
    if (!validToken) {
      oauthService.initCodeFlow();
      return false;
    }
    oauthService.setupAutomaticSilentRefresh();
    return true;
  });
};
