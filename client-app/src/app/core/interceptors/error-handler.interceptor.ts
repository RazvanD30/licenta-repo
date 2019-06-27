import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {AuthenticationService} from '../services/authentication.service';
import {catchError} from 'rxjs/operators';
import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material';

@Injectable()
export class ErrorHandlerInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService,
              private snackBar: MatSnackBar) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).timeout(30000).pipe(catchError(response => {
      const err = response.error;
      if (err.status === 401) {
        // auto logout if 401 response returned from api
        this.authenticationService.logout();
        // location.reload();
      }
      const errors: string[] = err.errors;
      return throwError(errors);
    }));
  }


}
