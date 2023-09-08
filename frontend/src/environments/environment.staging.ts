import { AuthConfig } from 'angular-oauth2-oidc';

export const environment = {
  production: true,
  staging: true,
  oauth: {
    decreaseExpirationBySec: 30,
    clearHashAfterLogin: true,
    issuer: '',
    strictDiscoveryDocumentValidation: false,
    redirectUri: `https://okr.ocp-staging.cloudscale.puzzle.ch:/auth/keycloakopenid/callback`,
    scope: '',
    clientId: 'pitc_okr_prod',
    responseType: 'code',
    showDebugInformation: true,
  } as AuthConfig,
};
