import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from '@angular/common/http';
import {
  catchError,
  filter,
  map,
  merge,
  mergeMap,
  Observable,
  of,
  take,
  timeout,
} from 'rxjs';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable()
export class OauthInterceptor implements HttpInterceptor {
  constructor(private oauthService: OAuthService) {}
  intercept(
    req: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    if (!req.url.match(/^(\/)?api/)) {
      return next.handle(req);
    }

    return merge(
      of(this.oauthService.getAccessToken()).pipe(filter((token) => !!token)),
      this.oauthService.events.pipe(
        filter((e) => e.type === 'token_received'),
        timeout(500),
        map((_) => this.oauthService.getAccessToken())
      )
    ).pipe(
      take(1),
      mergeMap((token) => {
        if (token) {
          const header = 'Bearer ' + token;
          const headers = req.headers.set('Authorization', header);
          req = req.clone({ headers });
        }

        return next.handle(req);
        // .pipe(catchError((err) => this.errorHandler.handleError(err)));
      })
    );
  }
}
