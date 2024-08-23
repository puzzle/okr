// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

import { LogLevel, OpenIdConfiguration } from 'angular-auth-oidc-client';

export const environment = {
  production: false,
  staging: false,
  oauth: {
    secureRoutes: ['http://localhost:4200/', 'http://localhost:8080/'],
    postLoginRoute: '/dashboard',
    // decreaseExpirationBySec: 30,
    // clearHashAfterLogin: true,
    issuer: 'http://localhost:8095/auth/realms/pitc',
    // strictDiscoveryDocumentValidation: false,
    redirectUrl: 'http://localhost:4200/callback',
    postLogoutRedirectUri: window.location.origin,
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
