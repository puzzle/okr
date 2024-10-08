import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { map, take, tap } from 'rxjs';

//Not used, can be deleted
export const authGuard: CanActivateFn = (route, state) => {
  const oidcSecurityService = inject(OidcSecurityService);
  // console.log(oidcSecurityService.isAuthenticated$.subscribe(console.log));

  return oidcSecurityService.isAuthenticated$.pipe(
    take(1),
    map(({ isAuthenticated }) => {
      // allow navigation if authenticated
      return isAuthenticated;

      // redirect if not authenticated
    }),
  );
};
