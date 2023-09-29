import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { catchError, filter, Observable, throwError } from 'rxjs';
import { NotifierService } from '../services/notifier.service';
import { Router } from '@angular/router';
import { drawerRoutes } from '../constantLibary';
import { ToasterService } from '../services/toaster.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(
    private notifierService: NotifierService,
    private router: Router,
    private toasterService: ToasterService,
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      filter((event) => event instanceof HttpResponse),
      catchError((response) => {
        this.toasterService.showError(response.error.message);
        if (drawerRoutes.some((route) => request.url.includes(route))) {
          this.notifierService.closeDetailSubject.next();
          this.router.navigate(['']);
        }
        return throwError(() => new Error(response));
      }),
    );
  }
}
