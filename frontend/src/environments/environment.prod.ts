import { AuthConfig } from 'angular-oauth2-oidc';

export const environment = {
  production: true,
  oauth: {
    issuer: 'https://sso.puzzle.ch/auth/realms/pitc',
    strictDiscoveryDocumentValidation: false,
    redirectUri: 'https://okr.ocp.cloudscale.puzzle.ch/auth/keycloakopenid/callback',
    scope: 'profile offline_access openid',
    clientId: 'pitc_okr_prod',
    responseType: 'code',
    showDebugInformation: true,
  } as AuthConfig,
};
