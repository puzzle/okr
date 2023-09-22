import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { catchError, filter, Observable, throwError } from 'rxjs';
import { NotifierService } from '../services/notifier.service';
import { Router } from '@angular/router';
import { drawerRoutes } from '../constantLibary';

@Injectable()
export class DrawerInterceptor implements HttpInterceptor {
  constructor(
    private notifierService: NotifierService,
    private router: Router,
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      filter((event) => event instanceof HttpResponse),
      catchError((error) => {
        if (drawerRoutes.some((route) => request.url.includes(route))) {
          this.notifierService.closeDetailSubject.next();
          this.router.navigate(['']);
        }
        return throwError(() => new Error(error));
      }),
    );
  }
}
