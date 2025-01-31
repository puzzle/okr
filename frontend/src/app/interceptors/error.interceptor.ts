import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { catchError, filter, Observable, tap, throwError } from 'rxjs';
import { Router } from '@angular/router';
import {
  DRAWER_ROUTES,
  ERROR_MESSAGE_KEY_PREFIX, MessageEntry, MessageStatusCode,
  SUCCESS_MESSAGE_KEY_PREFIX,
  SUCCESS_MESSAGE_MAP
} from '../shared/constant-library';
import { ToasterService } from '../services/toaster.service';
import { TranslateService } from '@ngx-translate/core';
import { HttpType } from '../shared/types/enums/http-type';
import { ToasterType } from '../shared/types/enums/toaster-type';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private router: Router,
    private toasterService: ToasterService,
    private translate: TranslateService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request)
      .pipe(filter((event) => event instanceof HttpResponse), tap((response) => {
        if (this.checkForToaster(response)) {
          const method = HttpType[request.method as keyof typeof HttpType];
          this.handleSuccessToaster(response, method);
        }
      }), catchError((response) => {
        if (this.checkForToaster(response)) {
          this.handleErrorToaster(response);
        }
        this.handleDrawerError(request);
        return throwError(() => new Error(response));
      }));
  }

  handleErrorToaster(response: any) {
    const errors = response.error.errors.map((error: any) => this.translate.instant(ERROR_MESSAGE_KEY_PREFIX + error.errorKey)
      .format(error.params));

    errors.forEach((error: string) => this.toasterService.showError(error));
  }

  handleDrawerError(request: any) {
    if (DRAWER_ROUTES.some((route) => request.url.includes(route))) {
      this.router.navigate(['']);
    }
  }

  handleSuccessToaster(response: any, method: HttpType) {
    const successMessageObj = this.getSuccessMessageKey(response.url, response.status, method);
    if (!successMessageObj) {
      return;
    }

    if (successMessageObj.key == 'OBJECTIVE.POST' && response.body.quarterId == 999) {
      successMessageObj.key += '_BACKLOG';
    }

    const message: string = this.translate.instant(SUCCESS_MESSAGE_KEY_PREFIX + successMessageObj.key);
    this.toasterService.showCustomToaster(message, successMessageObj.toasterType);
  }

  getSuccessMessageKey(url: string, statusCode: number, method: HttpType) {
    for (const key in SUCCESS_MESSAGE_MAP) {
      const value = SUCCESS_MESSAGE_MAP[key];
      if (!url.includes(key)) {
        continue;
      }

      const toasterMessage = this.findToasterMessageByMethod(method, value);
      if (toasterMessage === undefined) {
        return undefined;
      }

      const message = this.findMessageByStatusCode(statusCode, value, toasterMessage.keysForCode || []);
      if (message !== undefined) {
        return message;
      }

      const messageKey = value.KEY + '.' + method;
      return { key: messageKey,
        toasterType: ToasterType.SUCCESS };
    }
    return undefined;
  }

  findToasterMessageByMethod(method: HttpType, entry: MessageEntry) {
    for (const toasterMessage of entry.methods) {
      if (toasterMessage.method == method) {
        return toasterMessage;
      }
    }
    return undefined;
  }

  findMessageByStatusCode(statusCode: number, entry: MessageEntry, codeKeys: MessageStatusCode[]) {
    for (const codeKey of codeKeys) {
      if (codeKey.code == statusCode) {
        const messageKey = entry.KEY + '.' + codeKey.key;
        return { key: messageKey,
          toasterType: codeKey.toaster };
      }
    }
    return undefined;
  }

  checkForToaster(response: any): boolean {
    const requestURL = new URL(response.url);
    return window.location.hostname == requestURL.hostname && requestURL.pathname.startsWith('/api');
  }
}
