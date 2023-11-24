import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { catchError, filter, Observable, tap, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { drawerRoutes } from '../constantLibary';
import { ToasterService } from '../services/toaster.service';
import { TranslateService } from '@ngx-translate/core';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  NO_TOASTER_ROUTES = ['/token'];
  NO_TOASTER_SUCCESS_ROUTES = ['/action'];

  constructor(
    private router: Router,
    private toasterService: ToasterService,
    private translate: TranslateService,
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      filter((event) => event instanceof HttpResponse),
      tap((response) => {
        this.handleSuccess(response, request.method);
      }),
      catchError((response) => {
        this.handleErrorToaster(response);
        this.handleDrawerError(request);
        return throwError(() => new Error(response));
      }),
    );
  }

  handleErrorToaster(response: any) {
    if (this.NO_TOASTER_ROUTES.some((route) => response.url.includes(route))) {
      return;
    }

    const errors = response.error.errors.map((error: any) =>
      this.translate.instant('ERRORS.' + error.errorKey).format(error.params),
    );

    errors.forEach((error: string) => this.toasterService.showError(error));
  }

  handleDrawerError(request: HttpRequest<unknown>) {
    if (drawerRoutes.some((route) => request.url.includes(route))) {
      this.router.navigate(['']);
    }
  }

  handleSuccess(response: any, method: string) {
    const NO_TOASTER = this.NO_TOASTER_ROUTES.concat(this.NO_TOASTER_SUCCESS_ROUTES);
    if (NO_TOASTER.some((route) => response.url.includes(route))) {
      return;
    }

    if (response.status == 226) {
      this.toasterService.showWarn(this.translate.instant('ERRORS.ILLEGAL_CHANGE_OBJECTIVE_QUARTER'));
      return;
    }

    switch (method) {
      case 'POST': {
        this.toasterService.showSuccess('Element wurde erfolgreich erstellt');
        break;
      }
      case 'PUT': {
        this.toasterService.showSuccess('Element wurde erfolgreich aktualisiert');
        break;
      }
      case 'DELETE': {
        this.toasterService.showSuccess('Element wurde erfolgreich gelöscht');
        break;
      }
    }
  }
}
