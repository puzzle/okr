import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { catchError, filter, Observable, throwError } from 'rxjs';
import { NotifierService } from '../services/notifier.service';
import { Router } from '@angular/router';
import { drawerRoutes } from '../constantLibary';
import { ToasterService } from '../services/toaster.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  NO_ERROR_TOASTER_ROUTES = ['/token'];
  constructor(
    private notifierService: NotifierService,
    private router: Router,
    private toasterService: ToasterService,
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      filter((event) => event instanceof HttpResponse),
      catchError((response) => {
        this.handleErrorToaster(response);
        this.handleDrawerError(request);
        return throwError(() => new Error(response));
      }),
    );
  }

  handleErrorToaster(response: any) {
    if (!this.NO_ERROR_TOASTER_ROUTES.some((route) => response.url.includes(route))) {
      this.toasterService.showError(response.error.message);
    }
  }

  handleDrawerError(request: HttpRequest<unknown>) {
    if (drawerRoutes.some((route) => request.url.includes(route))) {
      this.notifierService.closeDetailSubject.next();
      this.router.navigate(['']);
    }
  }
}
