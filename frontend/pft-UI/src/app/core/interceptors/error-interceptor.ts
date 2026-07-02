import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const authService = inject(AuthService);
  return next(req).pipe(
    catchError((error) => {
      const isAuthEndpoint = req.url.includes('/auth/login') || req.url.includes('/auth/register');
      if (error.status === 401 && !isAuthEndpoint) {
        authService.logout();
        router.navigate(['/auth/login']);
      }
      return throwError(() => error);
    })
  );;
};
