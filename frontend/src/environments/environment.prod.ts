import { OpenIdConfiguration } from 'angular-auth-oidc-client';

export const environment = {
  production: true,
  staging: false,
  oauth: {
    decreaseExpirationBySec: 30,
    clearHashAfterLogin: true,
    secureRoutes: ['**', 'http://localhost:4200/', 'http://localhost:8080/', 'localhost:8080/'],
    authority: 'https://sso.puzzle.ch/auth/realms/pitc',
    strictDiscoveryDocumentValidation: false,
    customQueryParams: { response_modes_supported: ['query'] },
    // redirectUri: 'http://localhost:8080/auth/keycloakopenid/callback',
    // redirectUri: `${window.location.protocol}//${window.location.hostname}:${window.location.port}/auth/keycloakopenid/callback${window.location.search}`,
    redirectUrl: window.location.origin + '/callback',
    scope: 'profile openid offline_access',
    clientId: 'pitc_okr_prod',
    responseType: 'code',
    showDebugInformation: true,
    customParamsRefreshTokenRequest: {
      scope: 'openid profile offline_access',
    },
    ignoreNonceAfterRefresh: true, // this is required if the id_token is not returned
    triggerRefreshWhenIdTokenExpired: false,
  } as OpenIdConfiguration,
};
