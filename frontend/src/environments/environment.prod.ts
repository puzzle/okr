import { AuthConfig } from 'angular-oauth2-oidc';

export const environment = {
  production: true,
  staging: false,
  oauth: {
    decreaseExpirationBySec: 30,
    clearHashAfterLogin: true,
    issuer: 'https://sso.puzzle.ch/auth/realms/pitc',
    strictDiscoveryDocumentValidation: false,
    redirectUri: `${window.location.protocol}//${window.location.hostname}:${window.location.port}`,
    scope: 'profile openid',
    clientId: 'pitc_okr_prod',
    responseType: 'code',
    showDebugInformation: true
  } as AuthConfig
};
