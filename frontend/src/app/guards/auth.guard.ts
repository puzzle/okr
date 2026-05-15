import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';

export const authGuard: CanActivateFn = (route, state) => {
  const oAuthService = inject(OAuthService);
  const router = inject(Router);
  console.log('guard init: ' + window.location.href);
  return oAuthService.loadDiscoveryDocumentAndTryLogin({ preventClearHashAfterLogin: false })
    .then(async() => {
    // if the login failed initialize code flow
      const validToken = oAuthService.hasValidIdToken();
      if (!validToken) {
        oAuthService.initCodeFlow();
        return false;
      }
      oAuthService.setupAutomaticSilentRefresh();
      const params1 = [
        'scope',
        'code',
        'state',
        'session_state',
        'iss'
      ];
      //
      const params = new URLSearchParams(window.location.search);
      if (params1.some((param) => params.has(param))) {
        params1.forEach((param) => {
          if (params.has(param)) {
            params.delete(param);
          }
        });
        // return router.navigate([], {queryParams: params, queryParamsHandling: 'merge', skipLocationChange: true});
      }
      /*
       * redirect route to remove state query param. do it only, if this param exist to avoid infinite loop
       * if (route.queryParamMap.get('state')) {
       *   await router.navigateByUrl('');
       *   return false;
       * }
       */
      return true;
    });
};
