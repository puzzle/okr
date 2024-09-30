// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

import { LogLevel, OpenIdConfiguration } from 'angular-auth-oidc-client';

export const environment = {
  production: false,
  staging: false,
  oauth: {
    issuer: 'http://localhost:8095/auth/realms/pitc',
    redirectUrl: `${window.location.protocol}//${window.location.hostname}:${window.location.port}/callback`,
    postLogoutRedirectUri: window.location.origin,
    renewTimeBeforeTokenExpiresInSeconds: 30,
    scope: 'openid profile offline_access',
    clientId: '',
    responseType: 'code',
    logLevel: LogLevel.Debug,
    silentRenew: true,
    useRefreshToken: true,
    customParamsRefreshTokenRequest: {
      scope: 'openid profile offline_access',
    },
    ignoreNonceAfterRefresh: true, // this is required if the id_token is not returned
    triggerRefreshWhenIdTokenExpired: false,
  } as OpenIdConfiguration,
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
