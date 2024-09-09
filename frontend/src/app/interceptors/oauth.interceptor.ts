import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { filter, map, merge, mergeMap, Observable, of, take, timeout } from 'rxjs';
import { OidcSecurityService } from 'angular-auth-oidc-client';

@Injectable({
  providedIn: 'root',
})
export class OauthInterceptor implements HttpInterceptor {
  constructor(private oauthService: OidcSecurityService) {}
  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (!req.url.match(/^(\/)?api/)) {
      return next.handle(req);
    }

    return this.oauthService.getAccessToken().pipe(
      take(1),
      mergeMap((token) => {
        if (token) {
          const header = 'Bearer ' + token;
          const headers = req.headers.set('Authorization', header);
          req = req.clone({ headers });
        }

        return next.handle(req);
      }),
    );
  }
}
