import { AuthConfig } from 'angular-oauth2-oidc';

export const environment = {
  production: true,
  oauth: {
    issuer: 'https://sso.puzzle.ch/auth/realms/pitc',
    strictDiscoveryDocumentValidation: false,
    // redirectUri: 'http://localhost:8080/auth/keycloakopenid/callback',
    // redirectUri: `${window.location.protocol}//${window.location.hostname}:${window.location.port}/auth/keycloakopenid/callback${window.location.search}`,
    redirectUri: `${window.location.protocol}//${window.location.hostname}:${window.location.port}/auth/keycloakopenid/callback`,
    scope: 'profile offline_access openid',
    clientId: 'pitc_okr_prod',
    responseType: 'code',
    showDebugInformation: true,
  } as AuthConfig,
};
