import { OpenIdConfiguration } from 'angular-auth-oidc-client';

export const environment = {
  production: true,
  staging: false,
  oauth: {
    authority: 'https://sso.puzzle.ch/auth/realms/pitc',
    // redirectUri: `${window.location.protocol}//${window.location.hostname}:${window.location.port}/auth/keycloakopenid/callback${window.location.search}`,
    redirectUrl: window.location.origin + '/callback',
    postLogoutRedirectUri: window.location.origin,
    renewTimeBeforeTokenExpiresInSeconds: 30,
    scope: 'profile openid offline_access',
    clientId: 'pitc_okr_prod',
    responseType: 'code',
    // showDebugInformation: true,
    customParamsRefreshTokenRequest: {
      scope: 'openid profile offline_access',
    },
    ignoreNonceAfterRefresh: true, // this is required if the id_token is not returned
    triggerRefreshWhenIdTokenExpired: false,
  } as OpenIdConfiguration,
};
