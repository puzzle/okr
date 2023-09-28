import { AuthConfig } from 'angular-oauth2-oidc';

export const environment = {
  production: true,
  staging: true,
  oauth: {
    decreaseExpirationBySec: 30,
    clearHashAfterLogin: true,
    issuer: 'https://idp-mock-okr.ocp-internal.cloudscale.puzzle.ch/',
    strictDiscoveryDocumentValidation: false,
    redirectUri: `${window.location.protocol}//${window.location.hostname}:${window.location.port}/auth/keycloakopenid/callback`,
    scope: 'profile openid pitc preferred_username given_name family_name email',
    clientId: 'pitc_okr_prod',
    responseType: 'code',
    showDebugInformation: true,
  } as AuthConfig,
};
