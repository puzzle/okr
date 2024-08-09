import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { log } from '@angular-devkit/build-angular/src/builders/ssr-dev-server';

export const authGuard: CanActivateFn = (route, state) => {
  const oauthService = inject(OAuthService);
  const document = oauthService.loadDiscoveryDocument();

  if (oauthService.hasValidIdToken()) {
    return true;
  }

  return oauthService.loadDiscoveryDocumentAndTryLogin().then(async () => {
    // if the login failed initialize code flow
    let validToken = oauthService.hasValidIdToken();
    if (!validToken) {
      oauthService.initCodeFlow();
      return false;
    }
    oauthService.setupAutomaticSilentRefresh();
    location.hash = '';
    return true;
  });
};
