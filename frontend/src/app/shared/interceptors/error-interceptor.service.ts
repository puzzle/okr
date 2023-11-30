import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { catchError, filter, Observable, tap, throwError } from 'rxjs';
import { Router } from '@angular/router';
import {
  BLACKLIST_TOASTER_ROUTES_ERROR,
  DRAWER_ROUTES,
  ERROR_MESSAGE_KEY_PREFIX,
  HTTP_TYPE,
  SUCCESS_MESSAGE_KEY_PREFIX,
  SUCCESS_MESSAGE_MAP,
  WHITELIST_TOASTER_HTTP_METHODS_SUCCESS,
  WHITELIST_TOASTER_ROUTES_SUCCESS,
} from '../constantLibary';
import { ToasterService } from '../services/toaster.service';
import { TranslateService } from '@ngx-translate/core';
import { ToasterMessage } from '../types/toasterMessage';

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
      this.translate.instant(ERROR_MESSAGE_KEY_PREFIX + error.errorKey).format(error.params),
    );

    errors.forEach((error: string) => this.toasterService.showError(error));
  }

  handleDrawerError(request: HttpRequest<unknown>) {
    if (DRAWER_ROUTES.some((route) => request.url.includes(route))) {
      this.router.navigate(['']);
    }
  }

  handleSuccessToaster(response: any, method: string) {
    const requestURL = new URL(response.url);
    const successMessageObj = this.getSuccessMessageKey(requestURL.pathname, method, response.status);
    const message = this.translate.instant(SUCCESS_MESSAGE_KEY_PREFIX + '.' + successMessageObj.message);
    this.toasterService.showCustomToaster(message, successMessageObj.toasterType);
  }

  getSuccessMessageKey(url: string, method: string, statusCode: number): ToasterMessage {
    for (const key in SUCCESS_MESSAGE_MAP) {
      const value = SUCCESS_MESSAGE_MAP[key];
      if (!url.includes(key)) {
        continue;
      }

      for (let exception of value.exceptions) {
        if (exception.method == method && exception.statusCode == statusCode) {
          const messageKey = value.KEY + '.' + exception.key;
          return { message: messageKey, toasterType: 'WARN' };
        }
      }
      if (value.methods.includes(method as HTTP_TYPE)) {
        const messageKey = value.KEY + '.' + method;
        return { message: messageKey, toasterType: 'SUCCESS' };
      }
    }

    return { message: 'UNKNOWN', toasterType: 'SUCCESS' };
  }

  checkIfSuccessToasterIsShown(response: any, method: string): boolean {
    const requestURL = new URL(response.url);
    if (!WHITELIST_TOASTER_ROUTES_SUCCESS.some((route) => response.url.includes(route))) {
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
