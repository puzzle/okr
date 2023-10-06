import { AuthConfig } from 'angular-oauth2-oidc';

export const environment = {
  production: true,
  oauth: {
    decreaseExpirationBySec: 30,
    clearHashAfterLogin: true,
    issuer: '',
    strictDiscoveryDocumentValidation: false,
    redirectUri: `${window.location.protocol}//${window.location.hostname}:${window.location.port}`,
    scope: 'openid profile',
    clientId: 'pitc_okr_staging',
    responseType: 'code',
    showDebugInformation: true,
  } as AuthConfig,
};
