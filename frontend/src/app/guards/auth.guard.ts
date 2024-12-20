import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';

export const authGuard: CanActivateFn = (route, state) => {
  const oauthService = inject(OAuthService);
  const router = inject(Router);
  return oauthService.loadDiscoveryDocumentAndTryLogin()
    .then(async() => {
    // if the login failed initialize code flow
      const validToken = oauthService.hasValidIdToken();
      if (!validToken) {
        oauthService.initCodeFlow();
        return false;
      }
      oauthService.setupAutomaticSilentRefresh();
      // redirect route to remove state query param. do it only, if this param exist to avoid infinite loop
      if (route.queryParamMap.get('state')) {
        await router.navigateByUrl('');
        return false;
      }
      return true;
    });
};
