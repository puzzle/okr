import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { catchError, filter, Observable, tap, throwError } from 'rxjs';
import { Router } from '@angular/router';
import {
  WHITELIST_TOASTER_HTTP_METHODS_SUCCESS,
  DRAWER_ROUTES,
  HTTP_TYPES,
  SUCCESS_MESSAGE_KEYS,
  BLACKLIST_TOASTER_ROUTES_ERROR,
  BLACKLIST_TOASTER_ROUTES_SUCCESS,
  WHITELIST_TOASTER_ROUTES_SUCCESS,
} from '../constantLibary';
import { ToasterService } from '../services/toaster.service';
import { TranslateService } from '@ngx-translate/core';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(
    private router: Router,
    private toasterService: ToasterService,
    private translate: TranslateService,
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      filter((event) => event instanceof HttpResponse),
      tap((response) => {
        if (this.checkIfSuccessToasterIsShown(response, request.method)) {
          this.handleSuccessToaster(response, request.method);
        }
      }),
      catchError((response) => {
        this.handleErrorToaster(response);
        this.handleDrawerError(request);
        return throwError(() => new Error(response));
      }),
    );
  }

  handleErrorToaster(response: any) {
    if (BLACKLIST_TOASTER_ROUTES_ERROR.some((route) => response.url.includes(route))) {
      return;
    }

    const errors = response.error.errors.map((error: any) =>
      this.translate.instant('ERRORS.' + error.errorKey).format(error.params),
    );

    errors.forEach((error: string) => this.toasterService.showError(error));
  }

  handleDrawerError(request: HttpRequest<unknown>) {
    if (DRAWER_ROUTES.some((route) => request.url.includes(route))) {
      this.router.navigate(['']);
    }
  }

  handleSuccessToaster(response: any, method: string) {
    if (response.status == 226) {
      this.toasterService.showWarn(this.translate.instant('ERRORS.ILLEGAL_CHANGE_OBJECTIVE_QUARTER'));
      return;
    }

    const requestURL = new URL(response.url);
    const successMessageKey = this.getSuccessMessageKey(requestURL.pathname, method);
    const successMessage = this.translate.instant('SUCCESS.' + successMessageKey);
    this.toasterService.showSuccess(successMessage);
  }

  getSuccessMessageKey(url: string, method: string): string {
    for (const key in SUCCESS_MESSAGE_KEYS) {
      if (url.includes(key) && SUCCESS_MESSAGE_KEYS[key].methods.includes(method as HTTP_TYPES)) {
        return SUCCESS_MESSAGE_KEYS[key].KEY + '.' + method;
      }
    }
    return 'UNKNOWN';
  }

  checkIfSuccessToasterIsShown(response: any, method: string): boolean {
    const requestURL = new URL(response.url);
    if (!WHITELIST_TOASTER_ROUTES_SUCCESS.some((route) => response.url.includes(route))) {
      //Request on a not permitted route
      return false;
    }
    if (BLACKLIST_TOASTER_ROUTES_SUCCESS.some((route) => response.url.includes(route))) {
      //Request on a not permitted route
      return false;
    }
    if (window.location.host !== requestURL.host) {
      //Request on an external service
      return false;
    }
    if (!requestURL.pathname.startsWith('/api')) {
      //Request to our backend but not to our api
      return false;
    }
    return WHITELIST_TOASTER_HTTP_METHODS_SUCCESS.includes(method);
  }
}
