import { Injectable } from "@angular/core";
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from "@angular/common/http";
import { catchError, filter, Observable, tap, throwError } from "rxjs";
import { Router } from "@angular/router";
import {
  DRAWER_ROUTES,
  ERROR_MESSAGE_KEY_PREFIX,
  GJ_REGEX_PATTERN,
  SUCCESS_MESSAGE_KEY_PREFIX,
  SUCCESS_MESSAGE_MAP,
} from "../shared/constantLibary";
import { ToasterService } from "../services/toaster.service";
import { TranslateService } from "@ngx-translate/core";
import { HttpType } from "../shared/types/enums/HttpType";
import { ToasterType } from "../shared/types/enums/ToasterType";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(
    private router: Router,
    private toasterService: ToasterService,
    private translate: TranslateService,
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request)
      .pipe(
        filter((event) => event instanceof HttpResponse), tap((response) => {
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
        }),
      );
  }

  handleErrorToaster(response: any) {
    const errors = response.error.errors.map((error: any) =>
      this.translate.instant(ERROR_MESSAGE_KEY_PREFIX + error.errorKey)
        .format(error.params),
    );

    errors.forEach((error: string) => this.toasterService.showError(error));
  }

  handleDrawerError(request: any) {
    if (DRAWER_ROUTES.some((route) => request.url.includes(route))) {
      this.router.navigate([""]);
    }
  }

  handleSuccessToaster(response: any, method: HttpType) {
    const successMessageObj = this.getSuccessMessageKey(response.url, response.status, method);
    if (!successMessageObj) return;

    let messageKey = successMessageObj.key;
    const isBacklogQuarter = !GJ_REGEX_PATTERN.test(response.body?.quarterLabel);
    if (messageKey == "OBJECTIVE.POST" && isBacklogQuarter) {
      messageKey += "_BACKLOG";
    }
    const message = this.translate.instant(SUCCESS_MESSAGE_KEY_PREFIX + messageKey);
    this.toasterService.showCustomToaster(message, successMessageObj.toasterType);
  }

  getSuccessMessageKey(url: string, statusCode: number, method: HttpType) {
    for (const key in SUCCESS_MESSAGE_MAP) {
      const value = SUCCESS_MESSAGE_MAP[key];
      if (!url.includes(key)) continue;

      for (const toasterMessage of value.methods) {
        if (toasterMessage.method == method) {
          for (const codeKey of toasterMessage.keysForCode || []) {
            if (codeKey.code == statusCode) {
              const messageKey = value.KEY + "." + codeKey.key;
              return { key: messageKey, toasterType: codeKey.toaster };
            }
          }
          const messageKey = value.KEY + "." + method;
          return { key: messageKey, toasterType: ToasterType.SUCCESS };
        }
      }
    }
    return undefined;
  }

  checkForToaster(response: any): boolean {
    const requestURL = new URL(response.url);
    return window.location.hostname == requestURL.hostname && requestURL.pathname.startsWith("/api");
  }
}
