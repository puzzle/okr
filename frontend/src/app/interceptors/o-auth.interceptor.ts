import { Injectable, inject } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { filter, map, merge, mergeMap, Observable, of, take, timeout } from 'rxjs';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root'
})
export class OAuthInterceptor implements HttpInterceptor {
  private oAuthService = inject(OAuthService);


  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (!req.url.match(/^(\/)?api/)) {
      return next.handle(req);
    }

    return merge(of(this.oAuthService.getAccessToken())
      .pipe(filter((token) => !!token)), this.oAuthService.events.pipe(filter((e) => e.type === 'token_received'), timeout(500), map((_) => this.oAuthService.getAccessToken())))
      .pipe(take(1), mergeMap((token) => {
        if (token) {
          const header = 'Bearer ' + token;
          const headers = req.headers.set('Authorization', header);
          req = req.clone({ headers });
        }

        return next.handle(req);
        // .pipe(catchError((err) => this.errorHandler.handleError(err)));
      }));
  }
}
